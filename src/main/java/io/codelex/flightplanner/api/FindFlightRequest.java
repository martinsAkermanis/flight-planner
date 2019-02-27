package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class FindFlightRequest {

    private Airport from;
    private Airport to;
    private String carrier;
    private LocalDate departure;
    private LocalDate arrival;

    public FindFlightRequest(Airport from, Airport to, String carrier, LocalDate departure, LocalDate arrival) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departure = departure;
        this.arrival = arrival;
    }

    @JsonCreator
    public FindFlightRequest(
            @JsonProperty("from") Airport from,
            @JsonProperty("to") Airport to,
            @JsonProperty("departure") LocalDate departure,
            @JsonProperty("arrival") LocalDate arrivalTime) {
        this.from = from;
        this.to = to;
        this.departure = departure;
        this.arrival = arrivalTime;

    }

    public String getCarrier() {
        return carrier;
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

}
