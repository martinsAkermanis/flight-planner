package io.codelex.flightplanner.api;

import java.time.LocalDateTime;

public class Trip {

    private long id;
    private Airport from;
    private Airport to;
    private String carrier;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public Trip(long id, Airport from, Airport to, String carrier, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = LocalDateTime.now();
        this.arrivalTime = LocalDateTime.now().plusHours(1);
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
