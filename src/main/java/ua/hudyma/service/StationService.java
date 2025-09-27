package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Station;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.StationRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Log4j2
public class StationService {
    private final StationRepository stationRepository;

    public HttpStatus addAllStations(Station[] stations) {
        try {
            stationRepository.saveAll(Arrays.asList(stations));
        } catch (Exception e) {
            throw new EntityNotCreatedException("Cannot add all stations");
        }
        return HttpStatus.CREATED;
    }
}
