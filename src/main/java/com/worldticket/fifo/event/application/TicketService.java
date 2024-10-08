package com.worldticket.fifo.event.application;

import com.worldticket.fifo.event.domain.*;
import com.worldticket.fifo.event.dto.EventResponseDto;
import com.worldticket.fifo.event.dto.TicketDetailRequestDto;
import com.worldticket.fifo.event.dto.TicketRequestDto;
import com.worldticket.fifo.event.dto.TicketResponseDto;
import com.worldticket.fifo.event.infra.enums.Display;
import com.worldticket.fifo.event.infra.enums.TicketGrade;
import com.worldticket.fifo.event.infra.enums.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TicketService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public TicketService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public void saveTickets(TicketRequestDto ticketRequestDto) {
        // 1. 행사정보 확인
        if (!eventRepository.existsById(ticketRequestDto.getEventId())) {
            throw new IllegalArgumentException("해당하는 행사정보가 없습니다.");
        }
        // 2. Ticket 생성
        List<Ticket> tickets = ticketRequestDto.getTicketGradeDto().stream()
                .flatMap(typeDto -> IntStream.range(0, typeDto.getCount())
                        .mapToObj(i -> new Ticket(
                                new Event(ticketRequestDto.getEventId()), // 이벤트 정보
                                ticketRequestDto.getOpenTimeStamp(), // 티켓 오픈 시간
                                typeDto.getTicketGrade(), // TicketGrade 설정
                                i + 1, // 좌석 번호 (0부터 시작하므로 +1)
                                typeDto.getPrice(), // 가격 설정
                                UUID.randomUUID(),
                                Display.NO,
                                TicketStatus.ENROLLED
                        ))
                )
                .collect(Collectors.toList());
        // 3. 생성된 티켓 저장
        ticketRepository.saveAll(tickets);
    }

    public List<TicketResponseDto> findTickets(Long eventId) {
        return ticketRepository.findByEventEventIdAndTicketStatus(eventId, TicketStatus.ENROLLED).stream()
                .map(ticket -> new TicketResponseDto(
                        eventId,
                        ticket.getTicketGrade(),
                        ticket.getSeatNumber(),
                        ticket.getPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponseDto findTicketsDetail(TicketDetailRequestDto ticketDetailRequestDto) {
        Ticket ticket = ticketRepository.findByEventEventIdAndSeatNumberAndTicketGrade(
                        ticketDetailRequestDto.getEventId(),
                        ticketDetailRequestDto.getSeatNumber(),
                        ticketDetailRequestDto.getTicketGrade())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 티켓이 없습니다."));
        return TicketResponseDto.of(ticket);
    }
}
