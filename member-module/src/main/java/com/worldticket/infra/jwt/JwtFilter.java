package com.worldticket.infra.jwt;

import com.worldticket.application.TokenProvider;
import com.worldticket.infra.AuthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // Request 에서 토큰을 받아옴
        String accessToken = resolveToken(httpServletRequest);

        // 받아온 jwt 토큰을 validateToken 메서드로 유효성 검증
        if (tokenProvider.validateToken(accessToken)) {
            // 토큰이 정상이라면 Authentication 객체를 받아옴
            Authentication authentication = Optional.ofNullable(tokenProvider.getAuthentication(accessToken))
                    .orElseThrow(AuthorizationException::new);

            // SecurityContext 에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Request Header 에서 토큰 정보를 꺼내오기 위한 resolveToken 메서드 정의
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenType.ACCESS_TOKEN.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
