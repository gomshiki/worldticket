package com.worldticket.fifo.member.application;

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
        String email = tokenProvider.extractEmail(refreshToken);
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
