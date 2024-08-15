package com.worldticket.fifo.cart.application;

import com.worldticket.fifo.cart.domain.Cart;
import com.worldticket.fifo.cart.domain.CartRepository;
import com.worldticket.fifo.cart.dto.CartRequestDto;
import com.worldticket.fifo.cart.dto.CartResponseDto;
import com.worldticket.fifo.cart.infra.CartStatus;
import com.worldticket.fifo.commonservice.OrdersCommon;
import com.worldticket.fifo.event.domain.Ticket;
import com.worldticket.fifo.event.domain.TicketRepository;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import com.worldticket.fifo.member.domain.Member;
import com.worldticket.fifo.member.domain.MemberRepository;
import com.worldticket.fifo.member.dto.MemberRequestDto;
import com.worldticket.fifo.member.infra.MemberNotFoundException;
import com.worldticket.fifo.order.dto.OrderRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CartService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final TicketRepository ticketRepository;
    private final OrdersCommon ordersCommon;

    @Transactional
    public void saveCart(MemberRequestDto memberRequestDto, CartRequestDto cartRequestDto) {
        // 1. 회원 조회
        Member foundMember = memberRepository.findByEmail(memberRequestDto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        // 2. 티켓 확인
        Ticket foundTicket = ticketRepository.findFirstByEventEventIdAndTicketIdAndTicketStatus(
                        cartRequestDto.getEventId(), cartRequestDto.getTicketId(), TicketStatus.ENROLLED)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        foundTicket.changeTicketStatus(TicketStatus.RESERVED); // 티켓을 예약으로 변경
        ticketRepository.save(foundTicket); // 변경된 티켓 저장

        // 3. 기존 장바구니에 동일한 티켓이 있는지 검토
        if (cartRepository.existsByMemberAndTicket(foundMember, foundTicket)) {
            throw new IllegalArgumentException("해당 티켓은 이미 장바구니에 담겨있습니다.");
        }

        // 4. 새로운 장바구니 생성
        Cart newCart = Cart.builder()
                .member(foundMember)
                .ticket(foundTicket)
                .quantity(1)
                .cartStatus(CartStatus.ACTIVE)
                .build();

        cartRepository.save(newCart);
    }


    public List<CartResponseDto> findCarts(MemberRequestDto memberRequestDto) {
        List<Cart> foundCart = cartRepository.findByMemberMemberId(memberRequestDto.getMemberId());

        return foundCart.stream()
                .map(cart -> CartResponseDto.builder()
                        .cartId(cart.getCartId())
                        .ticketId(cart.getTicket().getTicketId())
                        .eventName(cart.getTicket().getEvent().getEventName())
                        .ticketPrice(cart.getTicket().getPrice())
                        .ticketGrade(cart.getTicket().getTicketGrade().name())
                        .build()
                ).toList();
    }


    @Transactional
    public void buyTickets(MemberRequestDto memberRequestDto, List<CartRequestDto> cartRequestDtos) {
        // 1. 회원 조회
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        // 2. 티켓 리스트 조회 및 검증
        List<Cart> cartsToBuy = cartRequestDtos.stream()
                .map(dto -> {
                    Ticket ticket = ticketRepository.findFirstByEventEventIdAndTicketIdAndTicketStatus(
                                    dto.getEventId(),
                                    dto.getTicketId(),
                                    TicketStatus.RESERVED)
                            .orElseThrow(() -> new IllegalArgumentException("유효한 티켓이 아닙니다."));
                    return cartRepository.findByMemberAndTicket(member, ticket)
                            .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 티켓이 없습니다."));
                })
                .collect(Collectors.toList());

        // 3. OrderRequest 생성
        List<OrderRequestDto> orderRequestDtos = cartsToBuy.stream().map(
                cart -> OrderRequestDto.builder()
                        .eventId(cart.getCartId())
                        .memberId(cart.getMember().getMemberId())
                        .seatNumber(cart.getTicket().getSeatNumber())
                        .ticketGrade(cart.getTicket().getTicketGrade().name())
                        .build()
        ).toList();


        // 3. 티켓 주문 처리 (Order 생성)
        ordersCommon.saveOrder(member.getMemberId(), orderRequestDtos);

        // 4. 장바구니에서 티켓 상태 변경
        cartsToBuy.forEach(
                cart -> cart.updateCartStatus(CartStatus.PURCHASED)
        );
        cartRepository.saveAll(cartsToBuy);
    }
}
