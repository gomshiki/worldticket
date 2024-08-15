package com.worldticket.fifo.globalutilities.config;

import com.worldticket.fifo.globalutilities.annotations.LoginMember;
import com.worldticket.fifo.member.application.MemberService;
import com.worldticket.fifo.member.dto.MemberRequestDto;
import com.worldticket.fifo.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@AllArgsConstructor
@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        } else {
            throw new IllegalArgumentException("확인할 토큰이 없습니다.");
        }

        MemberResponseDto foundMember = memberService.findMember(bearerToken);
        return new MemberRequestDto(foundMember.getMemberId(), foundMember.getEmail(), foundMember.getMemberName());
    }
}
