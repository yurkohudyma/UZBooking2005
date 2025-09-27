package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Route;
import ua.hudyma.dto.RouteRequestDto;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.StationRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Log4j2
public class RouteService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

    private Route mapToRoute(RouteRequestDto dto) {
        var departureStation = stationRepository
                .findByStationId(dto.departureStationId())
                .orElseThrow();
        var arrivalStation = stationRepository
                .findByStationId(dto.arrivalStationId())
                .orElseThrow();
        var route = new Route();
        route.setRouteId(dto.routeId());
        route.setDepartureStation(departureStation);
        route.setArrivalStation(arrivalStation);
        return route;

        //todo implement time and date for routes -> display in tickets
    }

    public HttpStatus addAll(RouteRequestDto[] routeRequestDto) {
        var routeList = Arrays
                .stream(routeRequestDto)
                .map(this::mapToRoute)
                .toList();
        try {
            routeRepository.saveAll(routeList);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Routes have not been added");
        }
        return HttpStatus.CREATED;
    }
}
