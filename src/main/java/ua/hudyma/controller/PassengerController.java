package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.domain.Passenger;
import ua.hudyma.service.PassengerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
@Log4j2
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<?> addAllPassengers (@RequestBody Passenger[] passengers){
        return ResponseEntity.status(passengerService.addAll (passengers))
                .body("Saved " + passengers.length + " passengers");
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generateRequestedNumberOfPassengers (
            @RequestParam Integer number){
        return ResponseEntity.status(passengerService
                        .generatePassengers (number))
                .body("Generated " + number + " passengers");
    }

    @GetMapping
    public ResponseEntity<Passenger> getPassenger (@RequestParam Long passengerId){
        return ResponseEntity.ok(passengerService.getUserById (passengerId));
    }
}
