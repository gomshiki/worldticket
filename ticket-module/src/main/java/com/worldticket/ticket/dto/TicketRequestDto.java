package com.worldticket.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {
    private Long eventId;
    private LocalDateTime openTimeStamp;
    private List<TicketGradeDto> ticketGradeDto;
}
