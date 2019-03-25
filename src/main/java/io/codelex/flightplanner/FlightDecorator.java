package io.codelex.flightplanner;

import io.codelex.flightplanner.api.*;
import io.codelex.flightplanner.weather.ForecastCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class FlightDecorator {

    private FlightService flightService;
    private final ForecastCache cache;

    public FlightDecorator(FlightService flightService, ForecastCache cache) {
        this.flightService = flightService;
        this.cache = cache;
    }

    List<TripWithWeather> findFlight(FindFlightRequest request) {
        return flightService.findFlight(request)
                .stream()
                .map(flight -> decorate(flight))
                .collect(Collectors.toList());
    }

    private TripWithWeather decorate(Flight flight) {
        Airport to = flight.getTo();
        Optional<Weather> weather = cache.checkCacheKey(
                to.getCity(),
                flight.getArrivalTime().toLocalDate()
        );

        return new TripWithWeather(
                flight.getId(),
                flight.getFrom(),
                flight.getTo(),
                flight.getCarrier(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                weather.orElse(null));
    }
}
