package com.worldticket.ticket.dto;

import com.worldticket.ticket.infra.TicketGrade;
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
