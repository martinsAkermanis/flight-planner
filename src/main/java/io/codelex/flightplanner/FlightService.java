package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
class FlightService {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Flight> flights = new ArrayList<>();

    List<Flight> getAllFlights() {
        return flights;
    }

    void clearAllFlights() {
        flights.clear();
    }

    synchronized Flight addFlight(AddFlightRequest request) {
        Flight flight = new Flight(
                id.incrementAndGet(),
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDepartureTime(),
                request.getArrivalTime()
        );
        if (isFlightPresent(request)) {
            throw new IllegalStateException();
        } else {
            flights.add(flight);
        }
        return flight;
    }

    Flight findFlightById(Long id) {
        return flights.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst().orElse(null);
    }

    synchronized void deleteFlightById(Long id) {
        flights.removeIf(flight -> flight.getId() == id);
    }

    List<Flight> findFromTo(String from, String to) {
        List<Flight> foundTrips = new ArrayList<>();
        if ((from == null) || (to == null)) {
            return foundTrips;
        } else if (from.isEmpty() && to.isEmpty()) {
            return foundTrips;
        }

        from = StringUtils.trimAllWhitespace(from).toLowerCase();
        to = StringUtils.trimAllWhitespace(to).toLowerCase();

        for (Flight trip : flights) {
            if (trip.getTo().getAirport().toLowerCase().contains(to)
                    && trip.getFrom().getAirport().toLowerCase().contains(from)) {
                foundTrips.add(trip);
            } else if (trip.getTo().getCountry().toLowerCase().contains(to)
                    && trip.getFrom().getCountry().toLowerCase().contains(from)) {
                foundTrips.add(trip);
            } else if (trip.getTo().getCity().toLowerCase().contains(to)
                    && trip.getFrom().getCity().toLowerCase().contains(from)) {
                foundTrips.add(trip);
            }
        }
        return foundTrips;
    }

    boolean isFlightPresent(AddFlightRequest request) {
        for (Flight flight : flights) {
            if (flight.getFrom().getAirport().equals(request.getFrom().getAirport())
                    && flight.getTo().getAirport().equals(request.getTo().getAirport())
                    && flight.getCarrier().toLowerCase().equals(request.getCarrier().toLowerCase())
                    && flight.getDepartureTime().equals(request.getDepartureTime())
                    && flight.getArrivalTime().equals(request.getArrivalTime())) {
                return true;
            }
        }
        return false;
    }

    List<Flight> findFlight(FindFlightRequest request) {
        List<Flight> foundFlight = new ArrayList<>();

        for (Flight flight : flights) {
            if (flight.getFrom().equals(request.getFrom())
                    && flight.getTo().equals(request.getTo())
                    && flight.getDepartureTime().toLocalDate().isEqual(request.getDeparture())
                    && flight.getArrivalTime().toLocalDate().isEqual(request.getArrival())) {
                foundFlight.add(flight);
            }
        }
        return foundFlight;
    }
}


