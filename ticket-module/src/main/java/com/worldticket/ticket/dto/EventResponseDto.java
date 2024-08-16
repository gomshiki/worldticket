package com.worldticket.ticket.dto;

import com.worldticket.ticket.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
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
