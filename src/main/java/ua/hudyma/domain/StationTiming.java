package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table(name = "station_timings")
@Data
public class StationTiming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stationId;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;
}
