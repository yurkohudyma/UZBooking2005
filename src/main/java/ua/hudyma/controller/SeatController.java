package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.domain.Seat;
import ua.hudyma.service.TrainCarService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {
    private final TrainCarService trainCarService;
    @GetMapping("/bind")
    public ResponseEntity<?> bindSeatsWithTrainCar (@RequestParam Integer trainCarNumber){
        return ResponseEntity
                .status(trainCarService.bindSeatsWithTraincarWhereMissing(trainCarNumber))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllTrainCarsSeats (@RequestParam Integer trainCarNumber){
        return ResponseEntity.ok(trainCarService.getAllTrainCarSoldSeats (trainCarNumber));
    }
}
