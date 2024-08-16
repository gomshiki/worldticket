package com.worldticket.ticket.dto;

import com.worldticket.ticket.domain.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VenueResponseDto {
    private Long venueId;
    private String venueName;
    private String location;
    private Integer capacity;

    public static VenueResponseDto of(Venue foundVenue) {
        return new VenueResponseDto(
                foundVenue.getVenueId(), foundVenue.getVenueName(), foundVenue.getLocation(), foundVenue.getCapacity()
        );
    }
}
