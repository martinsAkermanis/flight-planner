package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AddFlightRequest {

    private final Airport from;
    private final Airport to;
    private final String carrier;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;

    @JsonCreator
    public AddFlightRequest(
            @JsonProperty("from") Airport from,
            @JsonProperty("to") Airport to,
            @JsonProperty("carrier") String carrier,
            @JsonProperty("departureTime") LocalDateTime departureTime,
            @JsonProperty("arrivalTime") LocalDateTime arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = LocalDateTime.now();
        this.arrivalTime = LocalDateTime.now().plusHours(1);
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public String getCarrier() {
        return carrier;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
