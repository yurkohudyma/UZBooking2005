package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Timetable;
import ua.hudyma.dto.TicketRequestDto;
import ua.hudyma.dto.TimetableRequestDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.TimetableRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public HttpStatus addTimetable(TimetableRequestDto dto) {
        if (checkObligatoryFields(dto)){
            throw new DtoObligatoryFieldsAreNullException
                    ("Timetable obligatory fields NOT provided");
        }
        if (dto.closestDateAssigned().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("ClosestDate cannot be in the past");
        }
        var route = routeRepository
                .findByRouteId(dto.routeId())
                .orElseThrow();
        var timetable = new Timetable();
        timetable.setRoute(route);
        timetable.setFrequencyType(dto.frequencyType());
        timetable.setClosestDateAssigned(dto.closestDateAssigned());
        route.setTimetable(timetable);
        try {
            timetableRepository.save(timetable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return HttpStatus.CREATED;
    }

    private boolean checkObligatoryFields(TimetableRequestDto requestDto) {
        return requestDto.routeId() == null ||
                requestDto.closestDateAssigned() == null ||
                requestDto.frequencyType() == null;
    }
}
