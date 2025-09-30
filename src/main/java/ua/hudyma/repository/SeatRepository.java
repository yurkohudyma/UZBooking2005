package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Seat;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {


    List<Seat> findByTrainCarId(Integer trainCarNumber);
}
