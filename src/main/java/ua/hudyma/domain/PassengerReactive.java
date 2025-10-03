package ua.hudyma.domain;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Table(name = "passengers_reactive")
@Data
public class PassengerReactive {
    @Id
    private Long id;
    private String name;
}

