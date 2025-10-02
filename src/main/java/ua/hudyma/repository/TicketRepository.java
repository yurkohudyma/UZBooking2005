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

    boolean existsByRouteIdAndSeat_SeatIdAndSeat_TrainCar_OrderNumber(
            String routeId,
            String seatId,
            Integer trainCarId
    );


    List<Ticket> findByPassengerIdOrderByDepartureDateAsc(Long passengerId);

    Optional<Ticket> findFirstByPassengerIdOrderByDepartureDateAsc(Long passengerId);

    @Query("""
                SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
                FROM Ticket t
                WHERE t.routeId = :routeId
                  AND t.seat.seatId = :seatId
                  AND t.seat.trainCar.orderNumber = :trainCarId
            """)
    boolean existsByRouteIdAndSeatIdAndTrainCarId(
            @Param("routeId") String routeId,
            @Param("seatId") String seatId,
            @Param("trainCarId") Integer trainCarId
    );

    /**
     * SELECT
     * 	route_id,
     *     s.seat_id,
     *     order_number
     * 	FROM tickets t
     * 		join seats s on t.seat_id = s.id
     * 			join train_cars tc on s.train_car_id = tc.id
     * 				where s.seat_id = '99'
     * 					and route_id = '715K'
     * 						and tc.order_number = 1
     */


}
