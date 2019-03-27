package io.codelex.flightplanner.api;

import java.time.LocalDateTime;

public class TripWithWeather extends Flight {

    private final Weather weather;

    public TripWithWeather(Long id,
                           Airport from,
                           Airport to,
                           String carrier,
                           LocalDateTime departureTime,
                           LocalDateTime arrivalTime,
                           Weather weather) {
        super(id, from, to, carrier, departureTime, arrivalTime);
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
