package com.worldticket.domain;

import com.worldticket.application.BaseTimeEntity;
import com.worldticket.infra.OrdersStatus;
import com.worldticket.ticket.domain.Ticket;
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
