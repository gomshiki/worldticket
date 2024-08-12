package com.worldticket.fifo.event.dto;

import com.worldticket.fifo.event.infra.enums.TicketGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {
    private Long eventId;
    private TicketGrade ticketGrade;
    private Integer seatNumber;
    private Integer price;
}
