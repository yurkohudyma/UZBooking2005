package ua.hudyma.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record TicketResponseDto(
        String ticketId,
        String routeId,
        String seatId,
        BigDecimal ticketPrice,
        String passengerName,
        LocalDate departureDate,
        LocalTime departureTime

) {
}
