package ua.hudyma.dto;

import ua.hudyma.enums.FrequencyType;

import java.time.LocalDateTime;

public record TimetableRequestDto(
        String routeId,
        FrequencyType frequencyType,
        LocalDateTime closestDateAssigned
) {
}
