package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Timetable;
import ua.hudyma.dto.TimetableRequestDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreNullException;
import ua.hudyma.exception.EntityNotCreatedException;
import ua.hudyma.exception.TimetableNotUpdatedException;
import ua.hudyma.repository.RouteRepository;
import ua.hudyma.repository.TimetableRepository;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

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
        if (dto.closestDateAssigned().isBefore(now())){
            throw new IllegalArgumentException("ClosestDate cannot be in the past");
        }
        var route = routeRepository
                .findByRouteId(dto.routeId())
                .orElseThrow();
        var timetable = new Timetable();
        timetable.setRoute(route);
        timetable.setFrequencyType(dto.frequencyType());
        timetable.setClosestDepartureDateAssigned(dto.closestDateAssigned());
        route.setTimetable(timetable);
        try {
            timetableRepository.save(timetable);
        } catch (Exception e) {
            throw new EntityNotCreatedException("timetable not added");
        }
        return HttpStatus.CREATED;
    }

    private boolean checkObligatoryFields(TimetableRequestDto requestDto) {
        return requestDto.routeId() == null ||
                requestDto.closestDateAssigned() == null ||
                requestDto.frequencyType() == null;
    }

    @Transactional
    @Scheduled(fixedRate = 600_000)
    public void updateTimetableIfDue (){
        try {
            timetableRepository
                    .findAll()
                    .stream()
                    .filter(tt -> tt.getClosestDepartureDateAssigned().isBefore(now()))
                    .forEach(tt -> {
                        tt.setClosestDepartureDateAssigned(updateClosestDate(tt));
                        log.info("Timetable {} departure time has been updated to {}",
                                tt.getId(),
                                tt.getClosestDepartureDateAssigned());
                    });
        } catch (Exception e) {
            throw new TimetableNotUpdatedException("Timetable update failed");
        }
    }

    private LocalDateTime updateClosestDate(Timetable tt) {
        return switch (tt.getFrequencyType()) {
            case DAILY -> tt.getClosestDepartureDateAssigned().plusDays(1);
            case ODD_EVEN ->  tt.getClosestDepartureDateAssigned().plusDays(2);
            case SPECIAL -> tt.getClosestDepartureDateAssigned();
        };
    }
}
