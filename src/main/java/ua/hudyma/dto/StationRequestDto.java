package ua.hudyma.dto;

import java.math.BigDecimal;

public record StationRequestDto(
        String stationId,
        String name,
        BigDecimal lat,
        BigDecimal lon) {
}
