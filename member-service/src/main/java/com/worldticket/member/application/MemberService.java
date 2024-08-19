package com.worldticket.member.application;

import com.worldticket.member.domain.Member;
import com.worldticket.member.domain.MemberRepository;
import com.worldticket.member.dto.LoginRequestDto;
import com.worldticket.member.dto.MemberEnrollRequestDto;
import com.worldticket.member.dto.MemberResponseDto;
import com.worldticket.common.exception.AuthorizationException;
import com.worldticket.common.exception.DataNotFoundException;
import com.worldticket.common.exception.MemberNotFoundException;
import com.worldticket.common.infra.EncryptUtil;
import com.worldticket.common.util.RedisProvider;
import com.worldticket.common.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;
    private final TokenProvider tokenProvider;
    private final RedisProvider redisProvider;

    public MemberResponseDto createMember(MemberEnrollRequestDto memberEnrollRequestDto) {
        Member member = MemberEnrollRequestDto.from(memberEnrollRequestDto);
        String encryptPassword = encryptUtil.encryptPassword(member.getPassword());
        member.encryptPassword(encryptPassword);
        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.builder()
                .email(savedMember.getEmail())
                .memberName(savedMember.getMemberName())
                .memberNumber(savedMember.getMemberNumber())
                .address1(savedMember.getAddress1())
                .address2(savedMember.getAddress2())
                .createAt(savedMember.getCreateAt())
                .phoneNumber(savedMember.getPhoneNumber())
                .build();
    }

    public MemberResponseDto getMemberByMemberNumber(UUID memberNumber) {
        Member foundMember = memberRepository.findByMemberNumber(memberNumber).orElseThrow(MemberNotFoundException::new);
        return MemberResponseDto.from(foundMember);
    }

    public List<MemberResponseDto> getMemberByAll() {
        Iterable<Member> foundMembers = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
        foundMembers.forEach(
                member -> {
                    memberResponseDtos.add(MemberResponseDto.from(member));
                }
        );
        return memberResponseDtos;
    }

    public void login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(MemberNotFoundException::new);
        if (!encryptUtil.validatePassword(loginRequestDto.getPassword(), member.getPassword())) {
            throw new MemberNotFoundException();
        }
    }

    public void logout(String refreshToken) {
        // 토큰에서 email 추출
        String email = tokenProvider.extractEmail(refreshToken);
        // Redis에서 해당 이메일로 저장된 토큰 조회
        String foundRefreshToken = Optional.ofNullable(redisProvider.getData(email))
                .orElseThrow(() -> new DataNotFoundException("해당 회원의 정보가 없습니다."));

        // Redis에 저장된 토큰과 전달된 refreshToken이 일치하는지 확인
        if (!foundRefreshToken.equals(refreshToken)) {
            throw new AuthorizationException("저장된 회원정보과 일치하지 않습니다.");
        }

        // 토큰이 존재하고 일치하면 삭제
        redisProvider.deleteData(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
