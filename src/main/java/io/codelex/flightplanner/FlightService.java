package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;

import java.util.List;

public interface FlightService {

    Flight addFlight(AddFlightRequest request);

    List<Flight> search(String from, String to);

    Flight findFlightById(Long id);

    void deleteFlightById(Long id);

    void clearAllFlights();

    boolean isFlightPresent(AddFlightRequest request);

    List<Flight>  getAllFlights();

    List<Flight> findFlight(FindFlightRequest request);
}
