package com.worldticket.fifo.member.application;

import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.dto.MemberEnrollRequestDto;
import com.worldticket.fifo.member.dto.MemberEnrollResponseDto;
import com.worldticket.fifo.member.infra.EncryptUtil;
import com.worldticket.fifo.member.infra.JpaMemberRepository;
import com.worldticket.fifo.member.infra.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;

    public MemberService(JpaMemberRepository jpaMemberRepository, EncryptUtil encryptUtil) {
        this.memberRepository = jpaMemberRepository;
        this.encryptUtil = encryptUtil;
    }

    public MemberEnrollResponseDto enrollMember(MemberEnrollRequestDto memberEnrollRequestDto) {
        Member member = MemberEnrollRequestDto.from(memberEnrollRequestDto);
        String encryptPassword = encryptUtil.encryptPassword(member.getPassword());
        member.encryptPassword(encryptPassword);
        Member savedMember = memberRepository.save(member);
        return MemberEnrollResponseDto.from(savedMember);
    }
}
