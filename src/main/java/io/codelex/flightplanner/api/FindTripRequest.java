package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FindTripRequest {

    private Airport from;
    private Airport to;
    private String carrier;
    private LocalDate departure;
    private LocalDate arrival;

    @JsonCreator
    public FindTripRequest(
            @JsonProperty("from") Airport from,
            @JsonProperty("to") Airport to,
            @JsonProperty("departure") LocalDate departure,
            @JsonProperty("arrival") LocalDate arrivalTime) {
        this.from = from;
        this.to = to;
        this.departure = departure;
        this.arrival = arrivalTime;

    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public String getCarrier() {
        return carrier;
    }
}
