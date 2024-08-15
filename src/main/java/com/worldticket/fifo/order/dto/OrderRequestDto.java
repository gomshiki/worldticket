package com.worldticket.fifo.order.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private Long eventId;
    private Integer seatNumber;
    private String ticketGrade;

    @Setter
    private Long memberId;
}
