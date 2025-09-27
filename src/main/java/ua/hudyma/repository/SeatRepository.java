package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
