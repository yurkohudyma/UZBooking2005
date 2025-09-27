package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "routes")
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String routeId;
    @ManyToOne
    @JoinColumn(name = "departure_station_id")
    private Station departureStation;
    @ManyToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;
    @OneToOne
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;
}
