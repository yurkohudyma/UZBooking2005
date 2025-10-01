package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.RouteRequestDto;
import ua.hudyma.dto.RouteStationResponseDto;
import ua.hudyma.service.RouteService;
import ua.hudyma.service.TrainCarService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
@Log4j2
public class RouteController {
    private final RouteService routeService;
    private final TrainCarService trainCarService;

    @PostMapping
    public ResponseEntity<String> addAllRoutes (
            @RequestBody RouteRequestDto[] routeRequestDto){
        return ResponseEntity.status(routeService.addAll (routeRequestDto))
                .body("Saved " + routeRequestDto.length + " routes");
    }
    @GetMapping
    public ResponseEntity<List<RouteStationResponseDto>> showRoutesStations (
            @RequestParam String routeId){
        return ResponseEntity.ok(routeService.showRoutesStations(routeId));
    }
    @GetMapping("/trainCars")
    public ResponseEntity<List<String>> getAllRoutesTrainCars (
            @RequestParam String routeId){
        return ResponseEntity.ok(trainCarService
                .getAllRouteCoupledTrainCars(routeId));
    }

    @GetMapping("/setTrainCarToRoute")
    public ResponseEntity<HttpStatus> getAllRoutesTrainCars (
            @RequestParam String routeId,
            @RequestParam String trainCarId){
        return ResponseEntity.status(trainCarService
                .setTrainCarToRoute(routeId, trainCarId)).build();
    }
}
