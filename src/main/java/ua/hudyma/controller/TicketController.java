package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.service.TicketService;

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
}
