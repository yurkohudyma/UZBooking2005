package ua.hudyma.enums;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TrainCarType {
    SEAT("Sitting", "Sitting car, without sleeping places"),
    COUPE("Coupe", "4-berth compartment car"),
    SV("SV", "2-berth luxury compartment car"),
    LUX("Lux", "Premium class sleeping car"),
    PLATZKART("Platzkart", "Open-plan sleeping car"),
    SLEEPER("Sleeper", "General sleeping car"),
    DINING("Dining", "Dining car with meals and drinks"),
    BAGGAGE("Baggage", "Baggage compartment car"),
    STAFF("Staff", "Service/staff car");

    private final String displayName;
    private final String description;



}

