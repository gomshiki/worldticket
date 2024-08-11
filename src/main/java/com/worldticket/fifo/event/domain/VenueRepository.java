package com.worldticket.fifo.event.domain;

import java.util.Optional;

public interface VenueRepository {
    Venue save(Venue venue);

    Optional<Venue> findById(Long venueId);

    Optional<Venue> findByVenueName(String venueName);
}
