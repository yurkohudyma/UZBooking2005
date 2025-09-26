package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.hudyma.domain.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {}
