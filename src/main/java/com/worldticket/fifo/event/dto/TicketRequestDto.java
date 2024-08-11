package com.worldticket.fifo.event.dto;

import com.worldticket.fifo.event.infra.enums.Display;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
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
    private TicketStatus ticketStatus;
    private Display display;
}
