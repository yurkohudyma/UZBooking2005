package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;
import ua.hudyma.enums.TrainCarType;

import java.util.List;

@Entity
@Table (name = "train_cars")
@Data
public class TrainCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private TrainCarType trainCarType;
    @OneToMany(mappedBy = "trainCar")
    private List<Seat> seatList;
    private Integer orderNumber;

    //todo add automatic additional traincar coupling //TrainService
}
