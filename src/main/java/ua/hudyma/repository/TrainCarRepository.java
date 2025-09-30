package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.TrainCar;

public interface TrainCarRepository extends JpaRepository<TrainCar, Long> {
}
