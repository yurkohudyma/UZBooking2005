package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Station;
import ua.hudyma.domain.StationTiming;
import ua.hudyma.domain.Timetable;
import ua.hudyma.dto.StationTimingRequestDto;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.StationRepository;
import ua.hudyma.repository.StationTimingRepository;
import ua.hudyma.repository.TimetableRepository;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Log4j2
public class StationService {
    private final StationRepository stationRepository;
    private final StationTimingRepository stationTimingRepository;
    private final TimetableRepository timetableRepository;

    public HttpStatus addAllStationTimings(StationTimingRequestDto[] stationTimings) {
        var newStations = new ArrayList<Station>();
        var newStationTimings = new ArrayList<StationTiming>();
        Arrays.stream(stationTimings)
                .filter(this::checkObligatoryFields)
                .forEach(
                st -> {
                    var stationTiming = new StationTiming();
                    var timetable = timetableRepository
                            .findById(st.timetableId())
                            .orElseThrow(() ->
                                    new DtoObligatoryFieldsAreNullException(
                                            "Timetable " + st.timetableId() + " does NOT exist"));
                    stationTiming.setTimetable(timetable);
                    stationTiming.setArrivalTime(st.arrivalTime());
                    stationTiming.setDepartureTime(st.departureTime());
                    newStationTimings.add(stationTiming);

                    if (!stationRepository.existsByStationId(st.stationId())) {
                        var station = new Station();
                        station.setStationId(st.stationId());
                        station.setName(st.stationName());
                        station.setApronQuantity(st.apronQuantity());
                        newStations.add(station);
                    }
                }
        );
        try {
            stationRepository.saveAll(newStations);
            stationTimingRepository.saveAll(newStationTimings);
        } catch (Exception e) {
            throw new EntityNotCreatedException("Stations or StationTimings have not been created");
        }
        return HttpStatus.CREATED;
    }

    public HttpStatus addAllStations(Station[] stations) {
        try {
            stationRepository.saveAll(Arrays.asList(stations));
        } catch (Exception e) {
            throw new EntityNotCreatedException("Cannot add all stations");
        }
        return HttpStatus.CREATED;
    }

    private boolean checkObligatoryFields(StationTimingRequestDto requestDto) {
        return requestDto.timetableId() != null ||
                requestDto.stationId() != null ||
                requestDto.stationName() != null ||
                requestDto.arrivalTime() != null ||
                requestDto.departureTime() != null ||
                requestDto.apronQuantity() != null;
    }
}
