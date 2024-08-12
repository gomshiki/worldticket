package com.worldticket.fifo.event.domain;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {
    Event save(Event event);

    Optional<Event> findByEventName(String eventName);

    Optional<Event> findById(Long eventId);

    List<Event> findAll();

    boolean existsById(Long eventId);

    boolean existsByEventNameAndEventDate(String eventName, LocalDateTime eventDate);
}
