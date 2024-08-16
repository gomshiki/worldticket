package com.worldticket.ticket.dto;

import com.worldticket.ticket.domain.Ticket;
import com.worldticket.ticket.infra.TicketGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketResponseDto {
    private Long eventId;
    private TicketGrade ticketGrade;
    private Integer seatNumber;
    private Integer price;

    @Builder
    public TicketResponseDto(Long eventId, TicketGrade ticketGrade, Integer seatNumber, Integer price) {
        this.eventId = eventId;
        this.ticketGrade = ticketGrade;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public static TicketResponseDto of(Ticket ticket) {
        return new TicketResponseDto().builder()
                .eventId(ticket.getEvent().getEventId())
                .price(ticket.getPrice())
                .seatNumber(ticket.getSeatNumber())
                .ticketGrade(ticket.getTicketGrade())
                .build();
    }
}
