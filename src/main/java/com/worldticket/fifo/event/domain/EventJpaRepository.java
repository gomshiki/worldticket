package com.worldticket.fifo.event.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventJpaRepository extends CrudRepository<Event, Long>, EventRepository {
    Event save(Event event);

    Optional<Event> findByEventName(String eventName);

    Optional<Event> findById(Long eventId);

    List<Event> findAll();
}
