package com.worldticket.fifo.order.dto;

import com.worldticket.fifo.order.infra.OrdersStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class OrderResponseDto {
    private Long memberId; // 회원번호
    private Long ordersId; // 주문번호
    private OrdersStatus orderStatus; // 주문 상태
    private Integer ordersPrice; // 총 주문 금액
    private List<OrderLineResponseDto> ordersLines; // 각 주문 항목에 대한 정보
}
