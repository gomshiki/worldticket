package com.worldticket.ticket.application;

import com.worldticket.ticket.domain.Event;
import com.worldticket.ticket.domain.EventRepository;
import com.worldticket.ticket.domain.VenueRepository;
import com.worldticket.ticket.dto.EventRequestDto;
import com.worldticket.ticket.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public List<EventResponseDto> findAll() {
        return null;
    }

    public void save(EventRequestDto eventRequestDto) {
        // 1. 행사명과 날짜 중복 체크
        if (eventRepository.existsByEventNameAndEventDate(eventRequestDto.getEventName(), eventRequestDto.getEventDate())) {
            throw new IllegalArgumentException("이미 존재하는 행사명입니다.");
        }
        // 2. Venue 존재 여부 확인
        if (!venueRepository.existsById(eventRequestDto.getVenueId())) {
            throw new IllegalArgumentException("존재하지 않는 행사장입니다.");
        }
        // 3. Event 저장
        Event event = EventRequestDto.of(eventRequestDto);
        eventRepository.save(event);
    }

    public EventResponseDto findEventById(Long eventId) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 행사정보가 없습니다."));
        return EventResponseDto.of(foundEvent);
    }

    public List<EventResponseDto> findEventsAll(){
        List<Event> foundEvents = eventRepository.findAll();
        return foundEvents.stream()
                .map(EventResponseDto::of)
                .collect(Collectors.toList());
    }
}
