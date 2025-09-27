package ua.hudyma.dto;

public record RouteRequestDto(
        String departureStationId,
        String arrivalStationId,
        String routeId) {}
