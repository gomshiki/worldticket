package com.worldticket.dto;

import com.worldticket.ticket.infra.TicketGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderLineResponseDto {
    private Long ticketId;              // 티켓번호
    private Integer ticketPrice;        // 티켓가격
    private Integer seatNumber;         // 티켓좌석
    private TicketGrade ticketGrade;    // 티켓등급
    private String eventName;           // 행사이름
}
