package com.worldticket.fifo.event.ui;

import com.worldticket.fifo.event.application.TicketService;
import com.worldticket.fifo.event.dto.TicketDetailRequestDto;
import com.worldticket.fifo.event.dto.TicketRequestDto;
import com.worldticket.fifo.event.dto.TicketResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RequestMapping("/tickets")
@RestController
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTickets(@RequestBody TicketRequestDto ticketRequestDto) {
        ticketService.save(ticketRequestDto);
        return ResponseEntity.ok(Map.of("message", "티켓 저장이 완료됐습니다."));
    }

    @GetMapping("/find")
    public ResponseEntity<List<TicketResponseDto>> findTickets(@RequestParam Long eventId) {
        List<TicketResponseDto> ticketResponseDtoList = ticketService.findTickets(eventId);
        return ResponseEntity.ok(ticketResponseDtoList);
    }

    @PutMapping("/selectTicket")
    public ResponseEntity<?> findTicketsDetail(@RequestBody TicketDetailRequestDto ticketDetailRequestDto) {
        String ticketStatus = ticketService.findTicketsDetail(ticketDetailRequestDto);
        return ResponseEntity.ok(ticketStatus);
    }
}
