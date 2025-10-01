package ua.hudyma.dto;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.math.BigDecimal;

public record TicketRequestDto(
        String routeId,
        Integer trainOrderNumber,
        String seatId,
        BigDecimal ticketPrice,
        Long passengerId
) {
}
