package ua.hudyma.dto;

import java.math.BigDecimal;

public record TicketRequestDto(
        String routeId,
        String seatId,
        BigDecimal ticketPrice,
        Long passengerId
) {
}
