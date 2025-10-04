package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.hudyma.service.NasaService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nasa")
public class NasaReactiveController {
    private final NasaService nasaService;

    @GetMapping public Mono<String> getLargestPhotoUrl (){
        return nasaService.getLargestPhotoUrl();
    }
}
