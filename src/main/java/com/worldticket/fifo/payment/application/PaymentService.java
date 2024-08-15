package com.worldticket.fifo.payment.application;

import com.worldticket.fifo.event.infra.enums.TicketStatus;
import com.worldticket.fifo.order.domain.OrderRepository;
import com.worldticket.fifo.order.domain.Orders;
import com.worldticket.fifo.order.infra.OrdersStatus;
import com.worldticket.fifo.payment.domain.Payment;
import com.worldticket.fifo.payment.dto.PaymentRequestDto;
import com.worldticket.fifo.payment.infra.PaymentMethod;
import com.worldticket.fifo.payment.infra.PaymentStatus;
import com.worldticket.fifo.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void enrollPayment(PaymentRequestDto paymentRequestDto) {
        // 1. 주문 조회
        Orders foundOrder = orderRepository.findById(paymentRequestDto.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 주문 정보가 없습니다.")
        );

        // 2. Payment 엔티티 생성
        Payment payment = Payment.builder()
                .paymentAmount(foundOrder.getOrdersPrice())
                .orders(foundOrder)
                .paymentMethod(PaymentMethod.valueOf(paymentRequestDto.getPaymentMethod()))
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        paymentRepository.save(payment);

        // 4. 주문 상태 및 티켓 상태 변경
        foundOrder.setOrdersStatus(OrdersStatus.PAID);
        foundOrder.setPayment(payment);
        foundOrder.getOrdersLines().stream().forEach(
                ordersLine -> ordersLine.getTicket().setTicketStatus(TicketStatus.SOLD)
        );
    }
}
