package com.worldticket.ticket.dto;

import com.worldticket.ticket.infra.TicketGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailRequestDto {
    private Long eventId;
    private Integer seatNumber;
    private TicketGrade ticketGrade;
}
