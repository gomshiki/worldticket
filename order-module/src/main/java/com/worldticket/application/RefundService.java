package com.worldticket.application;

import com.worldticket.domain.*;
import com.worldticket.dto.RefundRequestDto;
import com.worldticket.infra.OrdersStatus;
import com.worldticket.infra.RefundStatus;
import com.worldticket.ticket.domain.TicketRepository;
import com.worldticket.ticket.infra.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefundService {
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void enrollRefund(RefundRequestDto refundRequestDto) {
        Orders foundOrder = orderRepository.findById(refundRequestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 정보가 없습니다."));

        Refund refund = Refund.builder()
                .orders(foundOrder)
                .refundAmount(foundOrder.getOrdersPrice())
                .payment(foundOrder.getPayment())
                .refundStatus(RefundStatus.REFUNDED)
                .build();

        refundRepository.save(refund);

        foundOrder.setOrdersStatus(OrdersStatus.REFUNDED);
        foundOrder.getOrdersLines().stream().forEach(
                ordersLine -> ordersLine.getTicket().setTicketStatus(TicketStatus.ENROLLED)
        );

        Payment payment = foundOrder.getPayment();
        payment.refundPayment();

    }
}
