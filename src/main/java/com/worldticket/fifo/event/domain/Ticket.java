package com.worldticket.fifo.event.domain;

import com.worldticket.fifo.cart.domain.Cart;
import com.worldticket.fifo.event.infra.enums.Display;
import com.worldticket.fifo.event.infra.enums.TicketGrade;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import com.worldticket.fifo.order.domain.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ticket")
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "open_timestamp", nullable = false)
    private LocalDateTime openTimeStamp;

    @Column(name = "ticket_grade", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketGrade ticketGrade;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "ticket_no", nullable = false)
    private UUID ticketNo;

    @Column(name = "ticket_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private TicketStatus ticketStatus;

    @Column(name = "display", nullable = false)
    @Enumerated(EnumType.STRING)
    private Display display;

    public Ticket(
            Event event, LocalDateTime openTimeStamp, TicketGrade ticketGrade, int seatNumber,
            int price, UUID ticketNo, Display display, TicketStatus ticketStatus
    ) {
        this.event = event;
        this.openTimeStamp = openTimeStamp;
        this.ticketGrade = ticketGrade;
        this.seatNumber = seatNumber;
        this.price = price;
        this.ticketNo = ticketNo;
        this.display = display;
        this.ticketStatus = ticketStatus;
    }

    // 상태 변경 메서드
    public void changeTicketStatus(TicketStatus newStatus) {
        this.ticketStatus = newStatus;
    }
}
