package com.worldticket.fifo.event.domain;

import com.worldticket.fifo.event.infra.enums.EventStatus;
import com.worldticket.fifo.event.infra.enums.EventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "event", uniqueConstraints = {
        @UniqueConstraint(columnNames = "event_date")}
)
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "event_name", length = 50, nullable = false)
    private String eventName;

    @Column(name = "event_type", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public Event(Long eventId) {
        this.eventId = eventId;
    }

    public Event(
            String eventName, EventType eventType, LocalDateTime eventDate,
            EventStatus eventStatus, int totalTickets, Venue venue
    ) {
        this(null, eventName, eventType, eventDate, null, null,
                eventStatus, totalTickets, null, venue
        );
    }

    public Event(Long eventId, String eventName, EventType eventType, LocalDateTime eventDate,
                 LocalDateTime createAt, LocalDateTime updateAt, EventStatus eventStatus,
                 int totalTickets, List<Ticket> tickets, Venue venue) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.eventStatus = eventStatus;
        this.totalTickets = totalTickets;
        this.tickets = tickets;
        this.venue = venue;
    }
}
