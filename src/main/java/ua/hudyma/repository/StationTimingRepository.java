package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.StationTiming;

public interface StationTimingRepository extends JpaRepository<StationTiming, Long> {
}
