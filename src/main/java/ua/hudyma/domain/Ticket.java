package ua.hudyma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticketId;
    private String routeId;
    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    @Positive
    private BigDecimal ticketPrice;
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
}
