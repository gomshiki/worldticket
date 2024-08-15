package com.worldticket.fifo.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private Long orderId;
    private String paymentMethod;
}
