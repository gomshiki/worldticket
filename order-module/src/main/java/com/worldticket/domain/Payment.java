package com.worldticket.domain;

import com.worldticket.application.BaseTimeEntity;
import com.worldticket.infra.PaymentMethod;
import com.worldticket.infra.PaymentStatus;
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
