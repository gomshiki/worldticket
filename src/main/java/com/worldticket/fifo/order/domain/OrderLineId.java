package com.worldticket.fifo.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class OrderLineId implements Serializable {
    @Setter
    @Column(name = "orders_id")
    private Long ordersId;

    @Column(name = "ticket_id")
    private Long ticketId;
}
