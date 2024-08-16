package com.worldticket.ticket.domain;

import com.worldticket.ticket.infra.TicketGrade;
import com.worldticket.ticket.infra.TicketStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Optional<Ticket> findByEventEventId(Long eventId);

    List<Ticket> findByEventEventIdAndTicketStatus(Long eventId, TicketStatus ticketStatus);

    // 단건으로 받을 경우: 이벤트 ID와 티켓 상태가 일치하는 첫 번째 티켓을 반환합니다.
    Optional<Ticket> findFirstByEventEventIdAndTicketIdAndTicketStatus(Long eventId, Long ticketId, TicketStatus ticketStatus);

    Optional<Ticket> findByEventEventIdAndSeatNumberAndTicketGrade(Long eventId, Integer seatNumber, TicketGrade ticketGrade);
}
