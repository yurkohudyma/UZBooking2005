package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;
import ua.hudyma.enums.TrainCarType;

@Entity
@Table (name = "train_cars")
@Data
public class TrainCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TrainCarType trainCarType;
}
