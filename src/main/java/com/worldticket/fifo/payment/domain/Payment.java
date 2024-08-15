package com.worldticket.fifo.payment.domain;

import com.worldticket.fifo.commonservice.BaseTimeEntity;
import com.worldticket.fifo.order.domain.Orders;
import com.worldticket.fifo.payment.infra.PaymentMethod;
import com.worldticket.fifo.payment.infra.PaymentStatus;
import com.worldticket.fifo.refund.domain.Refund;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "refund_id")
    private Refund refund;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    public void completePayment() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void refundPayment() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }
}
