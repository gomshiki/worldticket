package com.worldticket.fifo.refund.domain;

import com.worldticket.fifo.commonservice.BaseTimeEntity;
import com.worldticket.fifo.order.domain.Orders;
import com.worldticket.fifo.payment.domain.Payment;
import com.worldticket.fifo.refund.infra.RefundStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Refund extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders orders;

    @Column(name = "refund_amount", nullable = false)
    private Integer refundAmount;

    @Column(name = "refund_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;
}
