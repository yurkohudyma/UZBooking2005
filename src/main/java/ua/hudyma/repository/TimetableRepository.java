package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}
