package com.worldticket.fifo.member.infra;

import com.worldticket.fifo.member.domain.Member;

public interface MemberRepository {
    Member save(Member member);
}
