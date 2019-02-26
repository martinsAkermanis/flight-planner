package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Flight {

    private Long id;
    private Airport from;
    private Airport to;
    private String carrier;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    @JsonCreator
    public Flight(@JsonProperty ("id") long id,
                  @JsonProperty ("from")Airport from,
                  @JsonProperty ("to") Airport to,
                  @JsonProperty ("carrier") String carrier,
                  @JsonProperty ("departureTime")LocalDateTime departureTime,
                  @JsonProperty ("arrivalTime") LocalDateTime arrivalTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public long getId() {
        return id;
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
