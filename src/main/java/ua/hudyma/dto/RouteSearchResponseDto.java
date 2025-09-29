package ua.hudyma.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record RouteSearchResponseDto(
        String routeId,
        LocalDateTime departureDate,
        LocalTime departureTime
) {
}
