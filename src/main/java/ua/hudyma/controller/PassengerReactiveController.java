package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ua.hudyma.domain.PassengerReactive;
import ua.hudyma.service.PassengerReactiveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers_2")

public class PassengerReactiveController {
    private final PassengerReactiveService passengerReactiveService;

    @GetMapping
    public Flux<PassengerReactive> getAllPassengersReactive (){
        return passengerReactiveService.getAll ();
    }
}
