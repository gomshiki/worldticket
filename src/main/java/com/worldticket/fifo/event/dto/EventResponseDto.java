package com.worldticket.fifo.event.dto;

import com.worldticket.fifo.event.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class EventResponseDto {
    private Long eventId;
    private String eventName;
    private String eventType;
    private LocalDateTime eventDate;
    private Integer totalTickets;

    public static EventResponseDto of(Event foundEvent) {
        return new EventResponseDto(
                foundEvent.getEventId(), foundEvent.getEventName(), foundEvent.getEventType().toString(),
                foundEvent.getEventDate(), foundEvent.getTotalTickets()
        );
    }
}
