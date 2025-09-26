package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
@Data
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Profile profile;
    @OneToMany(mappedBy = "passenger",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ticket> ticketList = new ArrayList<>();
    @OneToMany(mappedBy = "passenger",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> notificationsList = new ArrayList<>();
    @Enumerated(value = EnumType.STRING)
    private PassengerStatus passengerStatus;
    public enum PassengerStatus {ACTIVE, DISABLED, SYSTEM}
}
