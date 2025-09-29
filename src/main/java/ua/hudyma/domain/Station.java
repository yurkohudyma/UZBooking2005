package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "stations")
@Data
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stationId;
    private String name;
    @OneToMany(mappedBy = "departureStation")
    private List<Route> departureRoutes;
    @OneToMany(mappedBy = "arrivalStation")
    private List<Route> arrivalRoutes;
    private BigDecimal lat;
    private BigDecimal lon;
    private Integer apronQuantity;
}
