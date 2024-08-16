package com.worldticket.application;

import com.worldticket.domain.*;
import com.worldticket.dto.OrderRequestDto;
import com.worldticket.infra.MemberNotFoundException;
import com.worldticket.infra.OrdersStatus;
import com.worldticket.ticket.domain.Ticket;
import com.worldticket.ticket.domain.TicketRepository;
import com.worldticket.ticket.infra.TicketGrade;
import com.worldticket.ticket.infra.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrdersCommon {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrdersLineRepository ordersLineRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void saveOrder(Long requestMemberId, List<OrderRequestDto> orderRequestDtoList) {
        // 1. 회원 확인
        Member member = findMemberById(requestMemberId);

        // 2. 티켓 정보 확인 및 OrdersLine 생성
        List<OrdersLine> ordersLines = createOrderLines(orderRequestDtoList);

        // 3. 주문 생성 및 저장
        Orders savedOrders = saveOrders(member, ordersLines);

        // 4. 티켓에 주문 정보 설정 및 저장
        updateTicketsWithOrder(ordersLines, savedOrders);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    private List<OrdersLine> createOrderLines(List<OrderRequestDto> orderRequestDtoList) {
        return orderRequestDtoList.stream()
                .map(orderRequestDto -> {
                    // 해당 티켓 저회
                    Ticket foundTicket = ticketRepository
                            .findByEventEventIdAndSeatNumberAndTicketGrade(
                                    orderRequestDto.getEventId(),
                                    orderRequestDto.getSeatNumber(),
                                    TicketGrade.valueOf(orderRequestDto.getTicketGrade()))
                            .orElseThrow(() -> new IllegalArgumentException("해당하는 티켓이 없습니다."));

                    // 티켓 상태가 ENROLLED가 아니면 예외발생
                    TicketStatus foundTicketTicketStatus = foundTicket.getTicketStatus();
                    if (foundTicketTicketStatus != TicketStatus.ENROLLED
                            && foundTicketTicketStatus != TicketStatus.RESERVED) {
                        throw new IllegalStateException("티켓이 이미 예약되었거나 판매된 상태입니다.");
                    }

                    // 티켓 상태를 RESERVED 로 변경
                    foundTicket.setTicketStatus(TicketStatus.ORDERED);

                    // OrdersLine 인스턴스 초기화
                    return OrdersLine.builder()
                            .id(new OrderLineId(null, foundTicket.getTicketId()))  // orderId는 나중에 설정
                            .ticket(foundTicket)
                            .price(foundTicket.getPrice()) // 각 티켓 가격 상태 변경
                            .quantity(1) // 기본적으로 1로 설정
                            .build();
                })
                .collect(Collectors.toList());
    }


    private Orders saveOrders(Member member, List<OrdersLine> ordersLines) {
        // 각 OrderLine 에 티켓 가격 합산
        int totalPrice = ordersLines.stream().mapToInt(OrdersLine::getPrice).sum();

        // Order 인스턴스 생성
        Orders orders = Orders.builder()
                .member(member) //회원 정보
                .ordersStatus(OrdersStatus.ONGOING) //
                .ordersPrice(totalPrice) //티켓 합산 가격
                .build();

        // Order 저장
        Orders savedOrders = orderRepository.save(orders);

        // OrderLine과 Order 연결
        ordersLines.forEach(ordersLine -> {
            ordersLine.setOrders(savedOrders);  //저장된 Order를 OrderLine에 매핑
            ordersLine.getId().setOrdersId(savedOrders.getOrdersId());   // OrderId 매핑
        });
        ordersLineRepository.saveAll(ordersLines);  //OrderLine 일괄 저장
        return savedOrders;
    }


    private void updateTicketsWithOrder(List<OrdersLine> ordersLines, Orders orders) {
        ordersLines.forEach(ordersLine -> {
            Ticket ticket = ordersLine.getTicket();
            ticket.setOrders(orders);
            ticketRepository.save(ticket); // 티켓 정보 업데이트
        });
    }
}
