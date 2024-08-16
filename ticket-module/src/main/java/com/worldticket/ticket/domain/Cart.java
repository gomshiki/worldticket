package com.worldticket.ticket.domain;

import com.worldticket.application.BaseTimeEntity;
import com.worldticket.domain.Member;
import com.worldticket.ticket.infra.CartStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart")
@Entity
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "quantity", nullable = false)
    private int quantity;  // 장바구니에 담긴 티켓 수량

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_status", nullable = false)
    private CartStatus cartStatus;  // 장바구니 상태 (예: ACTIVE, PURCHASED, EXPIRED)

    // 필요시 추가 메서드
    public void updateCartStatus(CartStatus newStatus) {
        this.cartStatus = newStatus;
    }
}
