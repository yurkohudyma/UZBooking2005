package ua.hudyma.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record RouteStationResponseDto(
        String stationName,
        LocalTime arrivalTime,
        LocalTime departureTime
) {
}
