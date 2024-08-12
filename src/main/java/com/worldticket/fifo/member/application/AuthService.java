package com.worldticket.fifo.member.application;

import com.worldticket.fifo.globalutilities.provider.RedisProvider;
import com.worldticket.fifo.globalutilities.provider.TokenProvider;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.TokenResponseDto;
import com.worldticket.fifo.member.infra.MemberNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
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


}
