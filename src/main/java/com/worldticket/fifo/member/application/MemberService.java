package com.worldticket.fifo.member.application;

import com.worldticket.fifo.globalutilities.annotations.DataNotFoundException;
import com.worldticket.fifo.globalutilities.provider.RedisProvider;
import com.worldticket.fifo.globalutilities.provider.TokenProvider;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.domain.MemberRepository;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.dto.MemberResponseDto;
import com.worldticket.fifo.member.infra.AuthorizationException;
import com.worldticket.fifo.member.infra.EncryptUtil;
import com.worldticket.fifo.member.infra.MemberNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;
    private final TokenProvider tokenProvider;
    private final RedisProvider redisProvider;

    public void enrollMember(MemberEnrollRequestDto memberEnrollRequestDto) {
        Member member = MemberEnrollRequestDto.from(memberEnrollRequestDto);
        String encryptPassword = encryptUtil.encryptPassword(member.getPassword());
        member.encryptPassword(encryptPassword);
        memberRepository.save(member);
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

    public MemberResponseDto findMember(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
        String email = tokenProvider.extractEmail(token);
        Member foundMember = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        return new MemberResponseDto(
                foundMember.getMemberId(), foundMember.getMemberName(), foundMember.getEmail()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
