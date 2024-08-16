package com.worldticket.ticket.dto;

import com.worldticket.ticket.domain.Event;
import com.worldticket.ticket.domain.Venue;
import com.worldticket.ticket.infra.EventStatus;
import com.worldticket.ticket.infra.EventType;
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
