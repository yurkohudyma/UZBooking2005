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
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                    continue;
                    //todo коротче, затик зі станціями... Жмеринка - Перемишль не знаходить тому, що
                    //todo на момент переходу для знаходження Перемишля цикл вже проітерований до кінця (((
                }
                if (st.getStationId().equals(arrivalStationId) && departureStation != null) {
                    arrivalStation = st;
                    break;
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
}
