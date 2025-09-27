package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Seat;
import ua.hudyma.domain.Ticket;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.PassengerRepository;
import ua.hudyma.repository.SeatRepository;
import ua.hudyma.repository.TicketRepository;
import ua.hudyma.util.IdGenerator;

@Service
@RequiredArgsConstructor
@Log4j2
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatRepository seatRepository;


    public HttpStatus addTicket(TicketRequestDto requestDto) {
        if (checkObligatoryField(requestDto)){
            throw new DtoObligatoryFieldsAreNullException("Some dto compulsory fields NOT provided");
        }
        var ticket = new Ticket();
        ticket.setRouteId(requestDto.routeId());
        ticket.setTicketPrice(requestDto.ticketPrice());
        var passenger = passengerRepository
                .findById(requestDto.passengerId())
                .orElseThrow();
        ticket.setPassenger(passenger);
        var seat = new Seat();
        //todo implement check of seat availability and vacancy
        seat.setSeatId(requestDto.seatId());
        seatRepository.save(seat);
        ticket.setSeat(seat);
        ticket.setTicketId(IdGenerator.generateUzTicketId());

        try {
            ticketRepository.save(ticket);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Ticket Not Added");
        }
        return HttpStatus.CREATED;
    }

    private boolean checkObligatoryField(TicketRequestDto requestDto) {
        return requestDto.routeId() == null ||
                requestDto.seatId() == null ||
                requestDto.passengerId() == null ||
                requestDto.ticketPrice() == null;
    }
}
