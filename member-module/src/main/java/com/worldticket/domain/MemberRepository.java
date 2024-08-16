package com.worldticket.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(String email);
}
