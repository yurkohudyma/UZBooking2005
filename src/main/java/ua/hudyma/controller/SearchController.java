package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.RouteSearchRequestDto;
import ua.hudyma.dto.RouteSearchResponseDto;
import ua.hudyma.service.SearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<RouteSearchResponseDto>> searchRoute (
            @RequestBody RouteSearchRequestDto requestDto){
        return ResponseEntity.ok(searchService
                .searchRouteBetweenStations(requestDto));
    }
}
