package com.worldticket.fifo.event.ui;

import com.worldticket.fifo.event.application.VenueService;
import com.worldticket.fifo.event.dto.VenueRequestDto;
import com.worldticket.fifo.event.dto.VenueResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
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
}
