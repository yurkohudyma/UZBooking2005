package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.hudyma.service.NasaService;

@RestController
@RequiredArgsConstructor
public class NasaReactiveController {
    private final NasaService nasaService;

    @GetMapping(value = "/nasa", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> getLargestPhotoUrl (){
        return nasaService
                .getLargestPhotoUrl()
                .flatMap(url -> WebClient.create(url)
                        .mutate().codecs(config -> config.defaultCodecs().maxInMemorySize(10_000_000)).build()
                        .get()
                        .exchangeToMono(resp -> resp.bodyToMono(byte[].class))
                );
    }

    @GetMapping("/url")
    public Mono<String> getUrl (){
        return nasaService.getLargestPhotoUrl();
    }
}