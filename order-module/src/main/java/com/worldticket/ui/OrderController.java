package com.worldticket.ui;

import com.worldticket.application.OrderService;
import com.worldticket.application.OrdersCommon;
import com.worldticket.dto.MemberRequestDto;
import com.worldticket.dto.OrderRequestDto;
import com.worldticket.dto.OrderResponseDto;
import com.worldticket.infra.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrdersCommon ordersCommon;

    @PostMapping("/tickets")
    public ResponseEntity<String> orderWishTicket(
            @LoginMember MemberRequestDto memberRequestDto, @RequestBody List<OrderRequestDto> orderRequestDtoList
    ) {
        ordersCommon.saveOrder(memberRequestDto.getMemberId(), orderRequestDtoList);
        return ResponseEntity.ok("티켓 주문이 완료됐습니다.");
    }

    @GetMapping("/find")
    public ResponseEntity<List<OrderResponseDto>> find(@LoginMember MemberRequestDto memberRequestDto) {
        return orderService.findOrder(memberRequestDto.getMemberId());
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrders(@RequestBody List<Long> ordersIdList) {
        orderService.cancelOrders(ordersIdList);
        return ResponseEntity.ok("주문 취소가 완료됐습니다.");
    }
}
