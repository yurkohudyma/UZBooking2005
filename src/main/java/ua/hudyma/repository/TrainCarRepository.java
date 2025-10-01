package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.TrainCar;

import java.util.Optional;

public interface TrainCarRepository extends JpaRepository<TrainCar, Long> {
    Optional<TrainCar> findByTrainCarId(String trainCarId);
}
