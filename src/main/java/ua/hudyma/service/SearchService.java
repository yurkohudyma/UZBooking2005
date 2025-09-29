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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

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
        var now = LocalTime.from(LocalDateTime.now());

        return routeRepository
                .findAll()
                .stream()
                .flatMap(route -> route.getTimetable().getInterStationsList().stream()
                        .filter(st ->
                                (st.getStationId().equals(departureStationId) &&
                                        st.getDepartureTime().plusMinutes(5).isAfter(now)) ||
                                        st.getStationId().equals(arrivalStationId)
                        )
                        .map(st -> new RouteSearchResponseDto(
                                route.getRouteId(),
                                LocalDate
                                        .from(st.getTimetable()
                                                .getClosestDepartureDateAssigned())
                                        .atStartOfDay(),
                                st.getArrivalTime()
                        ))
                )
                .toList();
    }


    /*@Transactional(readOnly = true)
    public List<RouteSearchResponseDto> searchRouteBetweenStations
            (RouteSearchRequestDto requestDto) {
        var departureStationId = requestDto.departureStationId();
        var arrivalStationId = requestDto.arrivalStationId();
        if (!stationRepository.existsByStationId(arrivalStationId)) {
            throw new IllegalArgumentException("Station " + arrivalStationId + " does NOT exist");
        }
        if (!stationRepository.existsByStationId(departureStationId)) {
            throw new IllegalArgumentException("Station " + departureStationId + " does NOT exist");
        }
        var routeListlist = routeRepository.findAll();
        var resultingSearchList = new ArrayList<RouteSearchResponseDto>();
        for (Route route : routeListlist) {
            var interstationList = route.getTimetable().getInterStationsList();
            for (StationTiming st : interstationList) {
                if (st.getDepartureTime()
                        .plusMinutes(5)
                        .isAfter(LocalTime.from(LocalDateTime.now())) &&
                    st.getStationId().equals(departureStationId) ||
                    st.getStationId().equals(arrivalStationId)) {
                        resultingSearchList.add(new RouteSearchResponseDto(
                                st.getTimetable().getRoute().getRouteId(),
                                LocalDate.from(st.getTimetable()
                                        .getClosestDepartureDateAssigned())
                                        .atStartOfDay(),
                                st.getArrivalTime()));
                    }
                }
            }
        return resultingSearchList;
    }*/
}
