package com.worldticket.fifo.event.domain;

import com.worldticket.fifo.event.infra.enums.Display;
import com.worldticket.fifo.event.infra.enums.TicketGrade;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Table(name = "ticket")
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    private UUID ticketId;

    @Column(name = "event_id", insertable = false, updatable = false)
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "open_timestamp", nullable = false)
    private LocalDateTime openTimeStamp;

    @Column(name = "ticketGrade", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketGrade ticketGrade;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "ticket_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @Column(name = "display", nullable = false)
    @Enumerated(EnumType.STRING)
    private Display display;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public Ticket(
            Event event, LocalDateTime openTimeStamp, TicketGrade ticketGrade, int seatNumber, int price,
            TicketStatus ticketStatus, Display display
    ) {
        this(null, event, openTimeStamp, ticketGrade, seatNumber, price, null, null,
                ticketStatus, display);
    }

    public Ticket(
            UUID ticketId, Event event, LocalDateTime openTimeStamp, TicketGrade ticketGrade, int seatNumber,
            int price, LocalDateTime createAt, LocalDateTime updateAt, TicketStatus ticketStatus, Display display
    ) {
        this.ticketId = ticketId;
        this.event = event;
        this.openTimeStamp = openTimeStamp;
        this.ticketGrade = ticketGrade;
        this.seatNumber = seatNumber;
        this.price = price;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.ticketStatus = ticketStatus;
        this.display = display;
    }

    // 상태 변경 메서드
    public void changeTicketStatus(TicketStatus newStatus) {
        this.ticketStatus = newStatus;
        this.updateAt = LocalDateTime.now(); // 상태 변경 시점 기록
    }

}
