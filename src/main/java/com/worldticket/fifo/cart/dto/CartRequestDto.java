package com.worldticket.fifo.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartRequestDto {
    private Long eventId;
    private Long ticketId;
}
