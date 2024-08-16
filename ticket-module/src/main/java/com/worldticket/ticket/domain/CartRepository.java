package com.worldticket.ticket.domain;

import com.worldticket.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findByMemberAndTicket(Member foundMember, Ticket foundTicket);

    boolean existsByMemberAndTicket(Member foundMember, Ticket foundTicket);

    List<Cart> findByMemberMemberId(Long memberId);
}
