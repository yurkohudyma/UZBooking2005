package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.hudyma.domain.PassengerReactive;
import ua.hudyma.service.PassengerReactiveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")

public class PassengerReactiveController {
    private final PassengerReactiveService passengerReactiveService;

    @GetMapping
    public Flux<PassengerReactive> getAllPassengersReactive (){
        return passengerReactiveService.getAll ();
    }

    @GetMapping("/create")
    public Mono<ResponseEntity<String>> createPassengers(@RequestParam Integer number) {
        return passengerReactiveService.createPassenger(number)
                .thenReturn(ResponseEntity.ok("Created " + number + " passengers"));
    }

}
