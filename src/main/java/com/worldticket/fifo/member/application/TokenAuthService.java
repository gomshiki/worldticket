package com.worldticket.fifo.member.application;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenAuthService implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";

    private final RedisService redisService;
    private final String secret;
    private final long accessExpiration;
    private final long refreshExpiration;
    private Key key;

    public TokenAuthService(
            RedisService redisService,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-time}") long accessExpiration,
            @Value("${jwt.refresh-expiration-time}") long refreshExpiration
    ) {
        this.redisService = redisService;
        this.secret = secret;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String payload) {
        final Claims claims = Jwts.claims().setSubject(payload);
        final Date now = new Date();
        final Date expireDate = new Date(now.getTime() + accessExpiration);

        // Access Token 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expireDate)
                .compact();
    }

    public String createRefreshToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + this.refreshExpiration);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        redisService.setDataExpire(payload, refreshToken,
                TimeUnit.MILLISECONDS.toMillis(expireDate.getTime() - now.getTime()));
        return refreshToken;
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

}
