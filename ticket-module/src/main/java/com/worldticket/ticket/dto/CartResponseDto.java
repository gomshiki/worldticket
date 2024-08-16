package com.worldticket.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long cartId;
    private Long ticketId;
    private String eventName;
    private Integer ticketPrice;
    private String ticketGrade;
}
