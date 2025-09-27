package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import ua.hudyma.enums.FrequencyType;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@Data
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "timetable")
    private Route route;
    @Enumerated(value = EnumType.STRING)
    private FrequencyType frequencyType;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime closestDateAssigned;

}
