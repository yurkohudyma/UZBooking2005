package ua.hudyma.dto;

import java.time.LocalTime;

public record StationTimingRequestDto(
        Long timetableId,
        String stationId,
        String stationName,
        LocalTime arrivalTime,
        LocalTime departureTime,
        Integer apronQuantity
) {
}
