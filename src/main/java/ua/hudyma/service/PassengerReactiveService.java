package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.hudyma.domain.PassengerReactive;
import ua.hudyma.repository.PassengerReactiveRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class PassengerReactiveService {
    private final PassengerReactiveRepository passengerReactiveRepository;

    public Flux<PassengerReactive> getAll() {
        return passengerReactiveRepository.findAll();
    }
}
