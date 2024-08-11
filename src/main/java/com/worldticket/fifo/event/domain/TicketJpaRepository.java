package com.worldticket.fifo.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<Ticket, UUID>, TicketRepository {
}
