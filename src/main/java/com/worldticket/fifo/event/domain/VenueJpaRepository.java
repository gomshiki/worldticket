package com.worldticket.fifo.event.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VenueJpaRepository extends CrudRepository<Venue, Long>, VenueRepository {
    Venue save(Venue venue);

    Optional<Venue> findById(Long venueId);
}
