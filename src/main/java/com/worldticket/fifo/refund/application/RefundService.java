package com.worldticket.fifo.refund.application;

import com.worldticket.fifo.event.domain.TicketRepository;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import com.worldticket.fifo.order.domain.OrderRepository;
import com.worldticket.fifo.order.domain.Orders;
import com.worldticket.fifo.order.infra.OrdersStatus;
import com.worldticket.fifo.payment.domain.Payment;
import com.worldticket.fifo.refund.domain.Refund;
import com.worldticket.fifo.refund.domain.RefundRepository;
import com.worldticket.fifo.refund.dto.RefundRequestDto;
import com.worldticket.fifo.refund.infra.RefundStatus;
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
