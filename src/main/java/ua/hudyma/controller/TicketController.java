package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.dto.TicketResponseDto;
import ua.hudyma.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
@Log4j2
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> addTicket (@RequestBody TicketRequestDto ticketRequestDto){
        return ResponseEntity.status(ticketService.addTicket (ticketRequestDto))
                .body("Ticket saved");
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getTickets (@RequestParam Long passengerId){
        return ResponseEntity.ok(ticketService.getTickets(passengerId));
    }
}
