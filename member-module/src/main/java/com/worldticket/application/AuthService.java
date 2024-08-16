package com.worldticket.application;

import com.worldticket.dto.LoginRequestDto;
import com.worldticket.dto.TokenRequestDto;
import com.worldticket.dto.TokenResponseDto;
import com.worldticket.infra.AuthorizationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RedisProvider redisProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenResponseDto issueToken(LoginRequestDto loginRequestDto) {
        log.info("토큰 발급 시작");
        // 로그인 유저정보를 이용하여 authenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // authenticationToken 객체를 이용하여 authenticate 메서드를 호출하여 인증정보를 받아옴
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // SecurityContext 에 인증정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // accessToken, refreshToken 생성(refresh 토큰은 redis에 저장)
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return new TokenResponseDto(accessToken, refreshToken);
    }


    public TokenResponseDto reissueToken(TokenRequestDto tokenRequestDto) {
        String email = tokenProvider.extractEmail(tokenRequestDto.getAccessToken());
        String storedRefreshToken = redisProvider.getData(email);
        if (tokenProvider.validateToken(storedRefreshToken)) {
            if (tokenRequestDto.getRefreshToken().equals(storedRefreshToken)) {
                // 토큰 재발급
                String newAccessToken = tokenProvider.createAccessToken(email);
                String newRefreshToken = tokenProvider.createRefreshToken(email);
                return new TokenResponseDto(newAccessToken, newRefreshToken);
            }
        }
        throw new AuthorizationException("토큰 재발급에 실패하였습니다.");
    }
}
