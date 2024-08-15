package com.worldticket.fifo.event.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends CrudRepository<Venue, Long> {
    Venue save(Venue venue);

    Optional<Venue> findById(Long venueId);
    
    boolean existsById(Long venueId);

    boolean existsByVenueName(String venueName);

    List<Venue> findAll();
}
