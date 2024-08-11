package com.worldticket.fifo.event.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "venue", uniqueConstraints = {@UniqueConstraint(columnNames = "venue_name")})
@Entity
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id")
    private Long venueId;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "venue")
    private List<Event> events;

    public Venue(Long venueId) {
        this(venueId, null, null, 0, null);
    }

    public Venue(String venueName, String location, int capacity) {
        this(null, venueName, location, capacity, null);
    }

    public Venue(Long venueId, String venueName, String location, int capacity, List<Event> events) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.location = location;
        this.capacity = capacity;
        this.events = events;
    }
}
