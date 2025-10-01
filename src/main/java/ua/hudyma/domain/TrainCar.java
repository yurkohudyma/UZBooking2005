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
    @Column(unique = true)
    private String trainCarId;
    @Enumerated(value = EnumType.STRING)
    private TrainCarType trainCarType;
    @OneToMany(mappedBy = "trainCar")
    private List<Seat> seatList;
    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;
    private Integer orderNumber;
}
