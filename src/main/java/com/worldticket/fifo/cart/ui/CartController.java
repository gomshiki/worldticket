package com.worldticket.fifo.cart.ui;

import com.worldticket.fifo.cart.application.CartService;
import com.worldticket.fifo.cart.dto.CartRequestDto;
import com.worldticket.fifo.cart.dto.CartResponseDto;
import com.worldticket.fifo.globalutilities.annotations.LoginMember;
import com.worldticket.fifo.member.dto.MemberRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
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
    public ResponseEntity<?> buyTicket(@LoginMember MemberRequestDto memberRequestDto, @RequestBody List<CartRequestDto> cartRequestDtos) {
        cartService.buyTickets(memberRequestDto, cartRequestDtos);
        return ResponseEntity.ok("주문이 완료됐습니다.");
    }

}
