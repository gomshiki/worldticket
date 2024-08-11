package com.worldticket.fifo.event.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);

    Optional<Event> findByEventNameAndEventDate(String eventName, LocalDateTime eventDate);

    Optional<Event> findById(Long id);

    List<Event> findAll();
}
