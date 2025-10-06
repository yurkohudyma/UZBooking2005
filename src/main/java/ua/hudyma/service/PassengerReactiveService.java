package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.hudyma.domain.PassengerReactive;
import ua.hudyma.repository.PassengerReactiveRepository;
import ua.hudyma.util.IdGenerator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class PassengerReactiveService {
    private final PassengerReactiveRepository passengerReactiveRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Flux<PassengerReactive> getAll() {
        return passengerReactiveRepository.findAll();
    }

    public Mono<Void> createPassenger(int number) {
        return Flux.range(1, number)
                .map(i -> {
                    var passenger = new PassengerReactive();
                    passenger.setName(IdGenerator.generateId(3, 20));
                    passenger.setBalance(BigDecimal
                            .valueOf(ThreadLocalRandom.current().nextDouble() * 1000));
                    return passenger;
                })
                .flatMap(passenger ->
                                r2dbcEntityTemplate.insert(PassengerReactive.class).using(passenger),
                        32 // concurrency, можеш змінити за потреби
                )
                //.doOnNext(p -> System.out.println("Inserted: " + p))
                .then();
    }

    /*public Mono<Void> createPassenger(int number) {
        var list = Stream
                .generate(
                        () -> {
                            var passenger = new PassengerReactive();
                            //passenger.setId(IdGenerator.generateLongId());
                            passenger.setName(IdGenerator
                                    .generateId(3, 20));
                            passenger.setBalance(BigDecimal
                                    .valueOf(ThreadLocalRandom.current().nextDouble() * 1000));
                            return passenger;
                        }
                )
                .limit(number)
                .toList();
        return Flux.fromIterable(list)
                .flatMap(passenger -> r2dbcEntityTemplate
                        .insert(PassengerReactive.class)
                        .using(passenger))
                //.doOnNext(p -> System.out.println("Inserted: " + p))
                .then();


    }*/
}
