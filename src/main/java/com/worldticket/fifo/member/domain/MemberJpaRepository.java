package com.worldticket.fifo.member.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberJpaRepository extends CrudRepository<Member, Long>, MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
