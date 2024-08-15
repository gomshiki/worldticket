package com.worldticket.fifo.event.domain;

import com.worldticket.fifo.commonservice.BaseTimeEntity;
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
public class Event extends BaseTimeEntity {
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

    public Event(Long eventId) {
        this.eventId = eventId;
    }

    public Event(
            String eventName, EventType eventType, LocalDateTime eventDate,
            EventStatus eventStatus, int totalTickets, Venue venue
    ) {
        this(null, eventName, eventType, eventDate, eventStatus, totalTickets, null, venue);
    }

    public Event(Long eventId, String eventName, EventType eventType, LocalDateTime eventDate,
                 EventStatus eventStatus, int totalTickets, List<Ticket> tickets, Venue venue) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventStatus = eventStatus;
        this.totalTickets = totalTickets;
        this.tickets = tickets;
        this.venue = venue;
    }
}
