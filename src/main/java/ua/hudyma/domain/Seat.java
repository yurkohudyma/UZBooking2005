package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatId;
    @OneToOne(mappedBy = "seat",
            cascade = CascadeType.ALL)
    @JsonBackReference
    private Ticket ticket;
    @ManyToOne
    @JoinColumn(name = "train_car_id")
    @JsonIgnore
    private TrainCar trainCar;
}
