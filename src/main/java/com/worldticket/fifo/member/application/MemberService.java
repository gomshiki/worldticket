package com.worldticket.fifo.member.application;

import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollResponseDto;
import com.worldticket.fifo.member.dto.TokenResponseDto;
import com.worldticket.fifo.member.infra.EncryptUtil;
import com.worldticket.fifo.member.domain.MemberRepository;
import com.worldticket.fifo.member.infra.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenAuthService tokenAuthService;
    private final EncryptUtil encryptUtil;
    private final RedisService redisService;

    public MemberService(MemberRepository jpaMemberRepository, TokenAuthService tokenAuthService, EncryptUtil encryptUtil, RedisService redisService) {
        this.memberRepository = jpaMemberRepository;
        this.tokenAuthService = tokenAuthService;
        this.encryptUtil = encryptUtil;
        this.redisService = redisService;
    }

    public void enrollMember(MemberEnrollRequestDto memberEnrollRequestDto) {
        Member member = MemberEnrollRequestDto.from(memberEnrollRequestDto);
        String encryptPassword = encryptUtil.encryptPassword(member.getPassword());
        member.encryptPassword(encryptPassword);
        memberRepository.save(member);
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        Member foundMember = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        if(!encryptUtil.validatePassword(loginRequestDto.getPassword(), foundMember.getPassword())){
            throw new IllegalArgumentException("유효하지 않은 아이디와 패스워드입니다.");
        }

        // 토큰 생성
        String accessToken = tokenAuthService.createAccessToken(email);
        String refreshToken = tokenAuthService.createRefreshToken(email);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public void logout(String refreshToken) {
        String email = tokenAuthService.extractEmail(refreshToken);
        redisService.deleteData(email);
    }
}
