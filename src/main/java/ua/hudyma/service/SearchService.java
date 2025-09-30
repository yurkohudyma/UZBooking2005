package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Route;
import ua.hudyma.domain.StationTiming;
import ua.hudyma.dto.RouteSearchRequestDto;
import ua.hudyma.dto.RouteSearchResponseDto;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.StationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public List<RouteSearchResponseDto> findRoutesByTransitStation(String stationId) {
        if (!stationRepository.existsByStationId(stationId)) {
            throw new IllegalArgumentException("Station " + stationId + " DOES not exist");
        }
        return routeRepository
                .findAll()
                .stream()
                .flatMap(route -> route
                        .getTimetable()
                        .getInterStationsList()
                        .stream()
                        .filter(st -> st.getStationId().equals(stationId)
                        ).map(st -> new RouteSearchResponseDto(
                                route.getRouteId(),
                                route.getTimetable().getClosestDepartureDateAssigned(),
                                st.getDepartureTime(),
                                st.getArrivalTime())
                        )).toList();
    }

    @Transactional(readOnly = true)
    public List<RouteSearchResponseDto> searchRouteBetweenStations(RouteSearchRequestDto requestDto) {
        var departureStationId = requestDto.departureStationId();
        var arrivalStationId = requestDto.arrivalStationId();
        if (!stationRepository.existsByStationId(arrivalStationId)) {
            throw new IllegalArgumentException("Station " + arrivalStationId + " does NOT exist");
        }
        if (!stationRepository.existsByStationId(departureStationId)) {
            throw new IllegalArgumentException("Station " + departureStationId + " does NOT exist");
        }
        var routesList = routeRepository.findAll();

        var resultList = new ArrayList<RouteSearchResponseDto>();
        for (Route route : routesList) {
            var interstationList = route.getTimetable().getInterStationsList();
            StationTiming departureStation = null;
            StationTiming arrivalStation = null;
            for (StationTiming st : interstationList) {
                if (st.getStationId().equals(departureStationId)
                        && st.getDepartureTime() != null
                        && route.getTimetable().getClosestDepartureDateAssigned()
                        .plusMinutes(5)
                        .isAfter(LocalDateTime.now())) {
                    departureStation = st;
                }
                if (departureStation != null) {
                    var arrivalMatch = findArrivalNewIterator(arrivalStationId, interstationList);
                    if (arrivalMatch != null) {
                        arrivalStation = arrivalMatch;
                        break;
                    }
                }
            }
            if (departureStation != null && arrivalStation != null) {
                var resultDto = new RouteSearchResponseDto(
                        route.getRouteId(),
                        route.getTimetable().getClosestDepartureDateAssigned(),
                        departureStation.getDepartureTime(),
                        arrivalStation.getArrivalTime()
                );
                resultList.add(resultDto);
            }
        }
        return resultList;
    }

    private StationTiming findArrivalNewIterator(String arrivalStationId, List<StationTiming> interstationList) {
        for (StationTiming st : interstationList) {
            if (st.getStationId().equals(arrivalStationId)) {
                return st;
            }
        }
        return null;
    }

    private boolean routeIsDue(Route route) {
        return route.getTimetable().getClosestDepartureDateAssigned()
                .plusMinutes(5)
                .isAfter(LocalDateTime.now());
    }
}
