package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.RouteRequestDto;
import ua.hudyma.service.RouteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
@Log4j2
public class RouteController {
    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<String> addAllRoutes (@RequestBody RouteRequestDto[] routeRequestDto){
        return ResponseEntity.status(routeService.addAll (routeRequestDto))
                .body("Saved " + routeRequestDto.length + " routes");
    }
}
