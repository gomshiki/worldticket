package com.worldticket.ticket.ui;

import com.worldticket.ticket.application.VenueService;
import com.worldticket.ticket.dto.VenueRequestDto;
import com.worldticket.ticket.dto.VenueResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/venue")
@RestController
public class VenueController {
    private final VenueService venueService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> save(@Valid @RequestBody VenueRequestDto venueRequestDto) {
        venueService.save(venueRequestDto);
        return ResponseEntity.ok(Map.of("message", "행사장 등록이 완료됐습니다"));
    }

    @GetMapping("/find")
    public ResponseEntity<VenueResponseDto> findVenueById(@RequestParam("venueId") Long venueId) {
        VenueResponseDto foundVenueDto = venueService.findVenueById(venueId);
        return ResponseEntity.ok(foundVenueDto);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<VenueResponseDto>> findVenues() {
        List<VenueResponseDto> venues = venueService.findVenues();
        return ResponseEntity.ok(venues);
    }
}
