package com.worldticket.fifo.member.application;

import com.worldticket.fifo.globalutilities.provider.RedisProvider;
import com.worldticket.fifo.globalutilities.provider.TokenProvider;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.dto.LoginRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.infra.EncryptUtil;
import com.worldticket.fifo.member.domain.MemberRepository;
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
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 회원아이디 또는 비밀번호가 없습니다."));

        if (!encryptUtil.validatePassword(loginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public void logout(String refreshToken) {
        String email = tokenProvider.extractEmail(refreshToken);
        redisProvider.deleteData(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));
    }
}
