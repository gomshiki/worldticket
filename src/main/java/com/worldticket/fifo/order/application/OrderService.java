package com.worldticket.fifo.order.application;

import com.worldticket.fifo.commonservice.OrdersCommon;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.dto.MemberRequestDto;
import com.worldticket.fifo.order.domain.*;
import com.worldticket.fifo.order.dto.OrderLineResponseDto;
import com.worldticket.fifo.order.dto.OrderResponseDto;
import com.worldticket.fifo.order.infra.OrdersStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrdersCommon ordersCommon;

    @Transactional
    public ResponseEntity<List<OrderResponseDto>> findOrder(Long requestMemberId) {
        // 1. 회원 확인
        Member member = ordersCommon.findMemberById(requestMemberId);

        // 2. 회원의 주문 내역 조회
        List<Orders> orders = findOrdersByMember(member);

        // 3. Orders를 OrderResponseDto 로 변환
        List<OrderResponseDto> orderResponseDtos = convertToOrderResponseDtos(orders, member);

        // 4. ResponseEntity로 반환
        return ResponseEntity.ok(orderResponseDtos);
    }

    @Transactional
    public void cancelOrders(List<Long> ordersIdList) {
        // 2. 주문 조회
        List<Orders> updateOrders = ordersIdList.stream().map(
                orderId -> {
                    Orders orders = orderRepository.findByOrdersIdAndOrdersStatus(orderId, OrdersStatus.ONGOING)
                            .orElseThrow(() -> new IllegalArgumentException("해당하는 주문 정보가 없습니다."));

                    orders.setOrdersStatus(OrdersStatus.CANCELLED);
                    return orders;
                }
        ).toList();

        // 3. 티켓 상태 변경
        updateOrders.stream().forEach(
                orders -> orders.getTickets().stream().forEach(
                        ticket -> ticket.changeTicketStatus(TicketStatus.ENROLLED)
                )
        );

    }

    private List<Orders> findOrdersByMember(Member member) {
        return orderRepository.findByMember(member);
    }

    private List<OrderResponseDto> convertToOrderResponseDtos(List<Orders> orders, Member member) {
        return orders.stream()
                .map(order -> {
                    List<OrderLineResponseDto> orderLineResponseDtos = convertToOrderLineResponseDtos(order);
                    return buildOrderResponseDto(order, member, orderLineResponseDtos);
                })
                .collect(Collectors.toList());
    }

    private List<OrderLineResponseDto> convertToOrderLineResponseDtos(Orders order) {
        return order.getOrdersLines().stream()
                .map(ordersLine -> OrderLineResponseDto.builder()
                        .ticketId(ordersLine.getTicket().getTicketId())               // 티켓번호
                        .ticketPrice(ordersLine.getPrice())                           // 티켓가격
                        .seatNumber(ordersLine.getTicket().getSeatNumber())           // 티켓좌석
                        .ticketGrade(ordersLine.getTicket().getTicketGrade())         // 티켓등급
                        .eventName(ordersLine.getTicket().getEvent().getEventName())  // 행사이름
                        .build())
                .collect(Collectors.toList());
    }

    private OrderResponseDto buildOrderResponseDto(Orders order, Member member, List<OrderLineResponseDto> orderLineResponseDtos) {
        return OrderResponseDto.builder()
                .memberId(member.getMemberId())          // 회원번호
                .ordersId(order.getOrdersId())           // 주문번호
                .orderStatus(order.getOrdersStatus())    // 주문상태
                .ordersPrice(order.getOrdersPrice())     // 총 주문 금액
                .ordersLines(orderLineResponseDtos)      // 각 주문 항목에 대한 정보
                .build();
    }
}
