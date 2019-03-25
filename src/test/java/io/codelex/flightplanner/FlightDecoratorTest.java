package io.codelex.flightplanner;

import io.codelex.flightplanner.api.*;
import io.codelex.flightplanner.weather.ForecastCache;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FlightDecoratorTest {

    FlightService flightService = Mockito.mock(FlightService.class);
    ForecastCache cache = Mockito.mock(ForecastCache.class);

    FlightDecorator decorator = new FlightDecorator(flightService, cache);
    LocalDate defaultDate = LocalDate.of(2019, 1, 1);


    @Test
    void should_combine_results_from_service_and_gateway() {
        //given
        FindFlightRequest req = new FindFlightRequest(
                new Airport("LV", "Riga", "RIX"),
                new Airport("SWE", "Stockholm", "ARN"),
                defaultDate,
                defaultDate.plusDays(1)
        );

        List<Flight> tripsFromService = Arrays.asList(
                new Flight(
                        1L,
                        new Airport("LV", "Riga", "RIX"),
                        new Airport("SWE", "Stockholm", "ARN"),
                        "Turkish",
                        defaultDate.atStartOfDay(),
                        defaultDate.plusDays(1).atStartOfDay()
                )
        );

        Weather weather = new Weather("Snow", 0, 0, 0);

        //when
        when(flightService.findFlight(req))
                .thenReturn(tripsFromService);

        when(cache.fetchForecast("Stockholm", defaultDate.plusDays(1)))
                .thenReturn(java.util.Optional.of(weather));

        List<TripWithWeather> trips = decorator.findFlight(req);

        //then
        TripWithWeather singleTrip = trips.get(0);

        assertEquals(1, trips.size());
        assertEquals("Snow", singleTrip.getWeather().getCondition());
    }
}