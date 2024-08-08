package com.worldticket.fifo.member.infra;

import com.worldticket.fifo.member.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface JpaMemberRepository extends CrudRepository<Member, Long>, MemberRepository {
    Member save(Member member);
}
