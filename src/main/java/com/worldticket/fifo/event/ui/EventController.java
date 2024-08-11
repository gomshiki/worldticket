package com.worldticket.fifo.event.ui;

import com.worldticket.fifo.event.application.EventService;
import com.worldticket.fifo.event.dto.EventRequestDto;
import com.worldticket.fifo.event.dto.EventResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RequestMapping("/events")
@RestController
public class EventController {
    private final EventService eventService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody EventRequestDto eventRequestDto) {
        eventService.save(eventRequestDto);
        return ResponseEntity.ok("행사 생성이 완료됐습니다.");
    }

    @GetMapping("/findall")
    public ResponseEntity<?> findEventsAll() {
        List<EventResponseDto> eventResponseDtoList = eventService.findEventsAll();
        return ResponseEntity.ok(eventResponseDtoList);
    }

    @GetMapping("/find")
    public ResponseEntity<EventResponseDto> findEvent(@RequestParam Long eventId){
        EventResponseDto foundEvent = eventService.findEventById(eventId);
        return ResponseEntity.ok(foundEvent);
    }
}
