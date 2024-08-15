package com.worldticket.fifo.order.domain;

import com.worldticket.fifo.event.domain.Ticket;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "orders_line")
public class OrdersLine {
    @EmbeddedId
    private OrderLineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ordersId")
    @JoinColumn(name = "orders_id")
    @Setter
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ticketId")
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
