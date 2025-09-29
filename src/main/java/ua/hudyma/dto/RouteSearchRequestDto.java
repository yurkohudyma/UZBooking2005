package ua.hudyma.dto;

public record RouteSearchRequestDto(
        String departureStationId,
        String arrivalStationId
) {
}
