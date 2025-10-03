package ua.hudyma.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.hudyma.domain.PassengerReactive;

public interface PassengerReactiveRepository extends ReactiveCrudRepository<PassengerReactive, Long> {
}
