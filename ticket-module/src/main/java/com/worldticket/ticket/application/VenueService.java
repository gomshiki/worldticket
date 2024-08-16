package com.worldticket.ticket.application;

import com.worldticket.ticket.domain.Venue;
import com.worldticket.ticket.domain.VenueRepository;
import com.worldticket.ticket.dto.VenueRequestDto;
import com.worldticket.ticket.dto.VenueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VenueService {
    private final VenueRepository venueRepository;


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

    public List<VenueResponseDto> findVenues() {
        List<Venue> venueList = venueRepository.findAll();
        return venueList.stream().map(VenueResponseDto::of).toList();
    }
}
