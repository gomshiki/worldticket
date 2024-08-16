package com.worldticket.application;

import com.worldticket.domain.OrderRepository;
import com.worldticket.domain.Orders;
import com.worldticket.domain.Payment;
import com.worldticket.domain.PaymentRepository;
import com.worldticket.dto.PaymentRequestDto;
import com.worldticket.infra.OrdersStatus;
import com.worldticket.infra.PaymentMethod;
import com.worldticket.infra.PaymentStatus;
import com.worldticket.ticket.infra.TicketStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
