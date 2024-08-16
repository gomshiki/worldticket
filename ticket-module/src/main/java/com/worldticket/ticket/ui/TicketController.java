package com.worldticket.ticket.ui;

import com.worldticket.ticket.application.TicketService;
import com.worldticket.ticket.dto.TicketDetailRequestDto;
import com.worldticket.ticket.dto.TicketRequestDto;
import com.worldticket.ticket.dto.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/tickets")
@RestController
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTickets(@RequestBody TicketRequestDto ticketRequestDto) {
        ticketService.saveTickets(ticketRequestDto);
        return ResponseEntity.ok("티켓 생성아 완료됐습니다.");
    }

    @GetMapping("/find")
    public ResponseEntity<List<TicketResponseDto>> findTickets(@RequestParam Long eventId) {
        List<TicketResponseDto> ticketResponseDtoList = ticketService.findTickets(eventId);
        return ResponseEntity.ok(ticketResponseDtoList);
    }

    @GetMapping("/selectTicket")
    public ResponseEntity<TicketResponseDto> findTicketsDetail(@RequestBody TicketDetailRequestDto ticketDetailRequestDto) {
        TicketResponseDto ticketsDetail = ticketService.findTicketsDetail(ticketDetailRequestDto);
        return ResponseEntity.ok(ticketsDetail);
    }


}
