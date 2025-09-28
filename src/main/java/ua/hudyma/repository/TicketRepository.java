package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hudyma.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT COUNT(s.seatId) FROM Ticket t JOIN t.seat s WHERE t.routeId = :routeId AND s.seatId = :seatId")
    int existsByRouteIdAndSeatId(@Param("seatId") String seatId, @Param("routeId") String routeId);
    Optional<Ticket> findFirstByPassengerIdOrderByDepartureDateAsc(Long passengerId);

}
