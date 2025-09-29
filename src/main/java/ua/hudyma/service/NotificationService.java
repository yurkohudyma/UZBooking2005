package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Notification;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.NotificationRepository;
import ua.hudyma.repository.PassengerRepository;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.TicketRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final PassengerRepository passengerRepository;

    public HttpStatus addNotification(Long passengerId) {
        if (passengerId == null) {
            throw new IllegalArgumentException("passId is NULL");
        }
        var passenger = passengerRepository.findById(passengerId).orElseThrow();
        var latestTicket = ticketRepository.findFirstByPassengerIdOrderByDepartureDateAsc(passengerId);
        if (latestTicket.isEmpty()) {
            throw new EntityNotCreatedException("passenger has no tickets");
        }
        var latestTicketOpt = latestTicket.get();
        var notification = new Notification();
        var routeId = latestTicketOpt.getRouteId();
        if (routeId == null || routeId.isEmpty()) {
            throw new IllegalArgumentException("routeId in ticket " + latestTicketOpt.getId() + " is EMPTY");
        }
        var route = routeRepository.findByRouteId(routeId).orElseThrow();
        var apronQuantity = route.getDepartureStation().getApronQuantity();
        var generatedApronNumber = generateApronNumber(apronQuantity);

        notification.setPassenger(passenger);
        var generatedTrackNumber = generateTrackNumber(generatedApronNumber, apronQuantity);
        notification.setText("Your train is at platform " + generatedApronNumber + ", track " + generatedTrackNumber);
        try {
            notificationRepository.save(notification);
        } catch (EntityNotCreatedException e) {
            throw new RuntimeException("Notification not CREATED");
        }
        return HttpStatus.CREATED;
    }

    private static int generateTrackNumber(int generatedApronNumber, int apronQty) {
        if (generatedApronNumber == 1 || generatedApronNumber == apronQty) {
            return 1;
        }
        return new SecureRandom().nextBoolean() ? 1 : 2;
    }

    private static int generateApronNumber(Integer apronQuantity) {
        var random = new SecureRandom().nextInt(apronQuantity);
        return random > 0 ? random : generateApronNumber(apronQuantity);
    }
}
