package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate departureDate;
    @Column(name = "departure_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    @CreationTimestamp
    private LocalDateTime createdOn;

}
