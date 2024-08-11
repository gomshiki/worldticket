package com.worldticket.fifo.event.application;

import com.worldticket.fifo.event.domain.*;
import com.worldticket.fifo.event.dto.TicketRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TicketService {
    private final EventRepository eventRepository;
    private final TicketJpaRepository ticketJpaRepository;

    public TicketService(EventJpaRepository eventJpaRepository, TicketJpaRepository ticketJpaRepository) {
        this.eventRepository = eventJpaRepository;
        this.ticketJpaRepository = ticketJpaRepository;
    }

    public void save(TicketRequestDto ticketRequestDto) {
        // 1. 행사정보 확인
        if (!eventRepository.findById(ticketRequestDto.getEventId()).isPresent()) {
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
                                ticketRequestDto.getTicketStatus(), // 티켓 상태
                                ticketRequestDto.getDisplay() // 표시 여부
                        ))
                )
                .collect(Collectors.toList());
        // 3. 생성된 티켓 저장
        ticketJpaRepository.saveAll(tickets);
    }
}
