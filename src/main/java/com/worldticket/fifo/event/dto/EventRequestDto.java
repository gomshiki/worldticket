package com.worldticket.fifo.event.dto;

import com.worldticket.fifo.event.domain.Event;
import com.worldticket.fifo.event.domain.Venue;
import com.worldticket.fifo.event.infra.enums.EventStatus;
import com.worldticket.fifo.event.infra.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private Long venueId;
    private String eventName;
    private String eventType;
    private LocalDateTime eventDate;
    private Integer totalTickets;
    private String eventStatus;

    public static Event of(EventRequestDto eventRequestDto) {
        return new Event(
                eventRequestDto.getEventName(), EventType.valueOf(eventRequestDto.getEventType()),
                eventRequestDto.getEventDate(), EventStatus.valueOf(eventRequestDto.getEventStatus()),
                eventRequestDto.getTotalTickets(),
                new Venue(eventRequestDto.getVenueId())
        );
    }
}
