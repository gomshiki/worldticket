package com.worldticket.fifo.event.domain;

import com.worldticket.fifo.event.infra.enums.TicketGrade;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {
    List<Ticket> findByEventIdAndTicketStatus(Long eventId, TicketStatus ticketStatus);

    boolean existsByEventId(Long eventId);

    Optional<Ticket> findByEventIdAndSeatNumberAndTicketGrade(Integer eventId, Integer seatNumber, TicketGrade ticketGrade);
}
