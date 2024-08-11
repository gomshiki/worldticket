package com.worldticket.fifo.event.ui;

import com.worldticket.fifo.event.application.TicketService;
import com.worldticket.fifo.event.dto.TicketRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
