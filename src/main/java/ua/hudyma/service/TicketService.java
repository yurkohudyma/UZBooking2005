package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Seat;
import ua.hudyma.domain.Ticket;
import ua.hudyma.domain.TrainCar;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.dto.TicketResponseDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.exception.SeatIsTakenException;
import ua.hudyma.exception.TicketOverbookingException;
import ua.hudyma.repository.PassengerRepository;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.SeatRepository;
import ua.hudyma.repository.TicketRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.Comparator.comparing;
import static ua.hudyma.util.IdGenerator.generateUzTicketId;

@Service
@RequiredArgsConstructor
@Log4j2
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final SeatRepository seatRepository;
    private final RouteRepository routeRepository;
    private final TrainCarService trainCarService;


    @Transactional
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
                .from(timetable.getClosestDepartureDateAssigned()));
        ticket.setDepartureTime(LocalTime
                .from(timetable.getClosestDepartureDateAssigned()));
        var seat = new Seat();

        var trainCarNumber = requestDto.trainOrderNumber();
        var soldTicketsQuantityForTrainCar = trainCarService
                .getAllTrainCarSoldSeats(trainCarNumber).size();
        TrainCar trainCar;
        try {
            trainCar = route
                    .getTimetable()
                    .getTrainCarList()
                    .get(trainCarNumber - 1);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch trainCar " + trainCarNumber +
                    " from Route DB, as none is coupled so far");
        }
        var quantityPerTrainCarType = trainCar.getTrainCarType().getSeats();
        if (quantityPerTrainCarType <= soldTicketsQuantityForTrainCar){
            throw new TicketOverbookingException("Trying to book a new ticket for train-car " + trainCarNumber + ", " +
                    "while none is available. Please wait until new TrainCar is coupled and booking system is refreshed");
        }
        var seatId = requestDto.seatId();
        if (isSeatTaken (seatId, routeId, trainCarNumber)){
            throw new SeatIsTakenException("Seat "+ seatId + " is taken in route " + routeId + " in traincar " + trainCarNumber);
        }
        seat.setSeatId(seatId);
        seat.setTrainCar(trainCar);
        seatRepository.save(seat);
        ticket.setSeat(seat);
        ticket.setTicketId(generateUzTicketId());

        try {
            ticketRepository.save(ticket);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Ticket Not Added");
        }
        return HttpStatus.CREATED;
    }

    public List<String> findAllTicketsSoldSeatsByRouteAndTrainCar(
            String routeId, Integer trainNumber) {
        return ticketRepository.findByRouteIdAndSeat_TrainCar_OrderNumber
                (routeId, trainNumber)
                .stream()
                .map(Ticket::getSeat)
                .map(Seat::getSeatId)
                .sorted()
                .toList();
    }

    public List<TicketResponseDto> getTickets(Long passengerId) {
        return ticketRepository
                .findByPassengerIdOrderByDepartureDateAsc(passengerId)
                .stream()
                .sorted(comparing(Ticket::getDepartureDate))
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

    private boolean isSeatTaken(String seatId, String routeId, Integer trainCarNumber) {
        return ticketRepository.existsByRouteIdAndSeat_SeatIdAndSeat_TrainCar_OrderNumber
                (routeId, seatId, trainCarNumber);
    }

    private boolean checkObligatoryFields(TicketRequestDto requestDto) {
        return requestDto.routeId() == null ||
                requestDto.seatId() == null ||
                requestDto.passengerId() == null ||
                requestDto.ticketPrice() == null;
    }
}
