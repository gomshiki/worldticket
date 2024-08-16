package com.worldticket.application;

import com.worldticket.infra.AuthorizationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";

    private final RedisProvider redisProvider;
    private final String secret;
    private final long accessExpiration;
    private final long refreshExpiration;
    private Key key;

    public TokenProvider(
            RedisProvider redisProvider,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-time}") long accessExpiration,
            @Value("${jwt.refresh-expiration-time}") long refreshExpiration
    ) {
        this.redisProvider = redisProvider;
        this.secret = secret;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        // authentication 권한 초기화
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpiration);

        // Access Token 생성
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireDate)
                .compact();
    }

    public String createAccessToken(String email) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpiration);

        // Access Token 생성
        return Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireDate)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpiration);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        redisProvider.setDataExpire(authentication.getName(), refreshToken,
                TimeUnit.MILLISECONDS.toMillis(expireDate.getTime() - now.getTime()));
        return refreshToken;
    }

    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpiration);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        redisProvider.setDataExpire(email, refreshToken,
                TimeUnit.MILLISECONDS.toMillis(expireDate.getTime() - now.getTime()));
        return refreshToken;
    }

    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었더라도 이메일을 추출
            return e.getClaims().getSubject();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
            return false;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        // 파라미터로 받아온 token을 이용해서 Claim 생성
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Claim 에서 권한정보들을 빼냄
        String authoritiesClaim = Optional.ofNullable(claims.get(AUTHORITIES_KEY))
                .map(Object::toString)
                .orElseThrow(() -> new AuthorizationException("토큰에 권한정보가 없습니다."));

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(authoritiesClaim.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 빼낸 권한정보를 이용해 유저를 생성
        User principal = new User(claims.getSubject(), "", authorities);

        // 유저정보, 토큰, 권한정보를 가진 Authentication 객체 리턴
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
