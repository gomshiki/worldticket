package com.worldticket.ticket.application;

import com.worldticket.application.OrdersCommon;
import com.worldticket.domain.Member;
import com.worldticket.domain.MemberRepository;
import com.worldticket.dto.MemberRequestDto;
import com.worldticket.dto.OrderRequestDto;
import com.worldticket.infra.MemberNotFoundException;
import com.worldticket.ticket.domain.Cart;
import com.worldticket.ticket.domain.CartRepository;
import com.worldticket.ticket.domain.Ticket;
import com.worldticket.ticket.domain.TicketRepository;
import com.worldticket.ticket.dto.CartRequestDto;
import com.worldticket.ticket.dto.CartResponseDto;
import com.worldticket.ticket.infra.CartStatus;
import com.worldticket.ticket.infra.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
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
