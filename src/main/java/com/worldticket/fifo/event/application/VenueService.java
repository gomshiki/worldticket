package com.worldticket.fifo.event.application;

import com.worldticket.fifo.event.domain.Venue;
import com.worldticket.fifo.event.domain.VenueRepository;
import com.worldticket.fifo.event.dto.VenueRequestDto;
import com.worldticket.fifo.event.dto.VenueResponseDto;
import org.springframework.stereotype.Service;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public void save(VenueRequestDto venueRequestDto) {
        Venue venue = VenueRequestDto.of(venueRequestDto);
        if (venueRepository.existsByVenueName(venue.getVenueName())) {
            throw new IllegalArgumentException("이미 저장되어 있는 장소명입니다.");
        }
        venueRepository.save(venue);
    }

    public VenueResponseDto findVenueById(Long venueId) {
        Venue foundVenue = venueRepository.findById(venueId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 행사장이 없습니다."));
        return VenueResponseDto.of(foundVenue);
    }
}
