package com.worldticket.fifo.order.domain;

import com.worldticket.fifo.commonservice.BaseTimeEntity;
import com.worldticket.fifo.event.domain.Ticket;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.order.infra.OrdersStatus;
import com.worldticket.fifo.payment.domain.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Builder
@Entity
public class Orders extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "orders")
    private List<Ticket> tickets;

    @Setter
    @OneToOne
    private Payment payment;

    @Setter
    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<OrdersLine> ordersLines;

    @Setter
    @Column(name = "orders_status")
    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;

    @Column(name = "orders_price")
    private Integer ordersPrice;
}
