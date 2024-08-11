package com.worldticket.fifo.event.dto;

import com.worldticket.fifo.event.infra.enums.TicketGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TicketGradeDto {
    private TicketGrade ticketGrade;
    private Integer count;
    private Integer price;
}
