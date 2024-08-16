package com.worldticket.ticket.ui;

import com.worldticket.dto.MemberRequestDto;
import com.worldticket.infra.LoginMember;
import com.worldticket.ticket.application.CartService;
import com.worldticket.ticket.dto.CartRequestDto;
import com.worldticket.ticket.dto.CartResponseDto;
import com.worldticket.ticket.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/cart")
@RestController
public class CartController {
    private final CartService cartService;

    @PostMapping("/enroll")
    public ResponseEntity<String> saveCart(
            @LoginMember MemberRequestDto memberRequestDto, @RequestBody CartRequestDto cartRequestDto
    ) {
        cartService.saveCart(memberRequestDto, cartRequestDto);
        return ResponseEntity.ok("장바구니에 저장이 완료됐습니다.");
    }

    @GetMapping("/findall")
    public ResponseEntity<List<CartResponseDto>> findCarts(@LoginMember MemberRequestDto memberRequestDto) {
        List<CartResponseDto> cartResponseDtoList = cartService.findCarts(memberRequestDto);
        return ResponseEntity.ok(cartResponseDtoList);
    }

    @PutMapping("/buy/ticket")
    public ResponseEntity<?> buyTicket(@LoginMember MemberRequestDto memberRequestDto, @RequestBody List<EventResponseDto.CartRequestDto> cartRequestDtos) {
        cartService.buyTickets(memberRequestDto, cartRequestDtos);
        return ResponseEntity.ok("주문이 완료됐습니다.");
    }

}
