package ua.hudyma.enums;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TrainCarType {
    SEAT("С", "Sitting car, without sleeping places", 18),
    PLATZKART("П", "Open-plan sleeping car", 54),
    COUPE("К", "4-berth compartment car", 36),
    SV("М", "2-berth luxury compartment car", 18),
    LUX("Л", "Premium class sleeping car", 10),
    SLEEPER("СВ", "General sleeping car", 40);
    private final String displayName;
    private final String description;
    private final int seats;

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getSeats() {
        return seats;
    }
}

