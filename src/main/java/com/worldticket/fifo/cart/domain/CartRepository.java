package com.worldticket.fifo.cart.domain;

import com.worldticket.fifo.event.domain.Ticket;
import com.worldticket.fifo.member.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findByMemberAndTicket(Member foundMember, Ticket foundTicket);

    boolean existsByMemberAndTicket(Member foundMember, Ticket foundTicket);

    List<Cart> findByMemberMemberId(Long memberId);
}
