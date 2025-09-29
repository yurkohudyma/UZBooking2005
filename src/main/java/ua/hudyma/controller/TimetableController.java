package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.TimetableRequestDto;
import ua.hudyma.service.TimetableService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timetables")
@Log4j2
public class TimetableController {
    private final TimetableService timetableService;

    @PostMapping
    public ResponseEntity<String> addTimetable (@RequestBody TimetableRequestDto dto){
        return ResponseEntity.status(timetableService.addTimetable (dto))
                .body("timetable added for route " + dto.routeId());
    }
}
