package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Seat;
import ua.hudyma.domain.Ticket;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.dto.TicketResponseDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.exception.SeatIsTakenException;
import ua.hudyma.repository.PassengerRepository;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.SeatRepository;
import ua.hudyma.repository.TicketRepository;
import ua.hudyma.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatRepository seatRepository;
    private final RouteRepository routeRepository;


    public HttpStatus addTicket(TicketRequestDto requestDto) {
        if (checkObligatoryFields(requestDto)){
            throw new DtoObligatoryFieldsAreNullException("Some dto compulsory fields NOT provided");
        }
        var ticket = new Ticket();
        var routeId = requestDto.routeId();
        ticket.setRouteId(routeId);
        ticket.setTicketPrice(requestDto.ticketPrice());
        var passenger = passengerRepository
                .findById(requestDto.passengerId())
                .orElseThrow();
        ticket.setPassenger(passenger);

        var route = routeRepository.findByRouteId(routeId).orElseThrow();
        var timetable = route.getTimetable();
        ticket.setDepartureDate(LocalDate
                .from(timetable.getClosestDateAssigned()));
        ticket.setDepartureTime(LocalTime
                .from(timetable.getClosestDateAssigned()));
        var seat = new Seat();

        var seatId = requestDto.seatId();
        if (isSeatTaken (routeId, seatId)){
            throw new SeatIsTakenException("Seat "+ seatId + " is taken");
        }
        seat.setSeatId(seatId);
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

    public List<TicketResponseDto> getTickets(Long passengerId) {
        return ticketRepository
                .findByPassengerIdOrderByDepartureDateAsc(passengerId)
                .stream()
                .map(ticket -> new TicketResponseDto(
                        ticket.getTicketId(),
                        ticket.getRouteId(),
                        ticket.getSeat().getSeatId(),
                        ticket.getTicketPrice(),
                        getPassengerName(ticket),
                        ticket.getDepartureDate(),
                        ticket.getDepartureTime()
                )).toList();
    }

    private static String getPassengerName(Ticket ticket) {
        return ticket.getPassenger().getProfile().getName().concat(" ").concat(
                ticket.getPassenger().getProfile().getSurname());
    }

    private boolean isSeatTaken(String seatId, String routeId) {
        return ticketRepository.existsByRouteIdAndSeatId(routeId, seatId) > 0;
    }

    private boolean checkObligatoryFields(TicketRequestDto requestDto) {
        return requestDto.routeId() == null ||
                requestDto.seatId() == null ||
                requestDto.passengerId() == null ||
                requestDto.ticketPrice() == null;
    }
}
