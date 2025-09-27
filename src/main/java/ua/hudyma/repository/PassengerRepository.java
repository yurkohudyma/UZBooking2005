package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
