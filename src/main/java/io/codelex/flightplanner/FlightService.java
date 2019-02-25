package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.FindTripRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
class FlightService {

    private Long sequence = 0L;
    private final List<Flight> flights = new ArrayList<>();

    Flight findFlight(FindTripRequest request) {
        return flightFinder(request);
    }

    void clearAllFlights() {
        flights.clear();
    }

    Flight addFlight(AddFlightRequest request) {
        Flight flight = new Flight(
                sequence++,
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

    public Flight findFlightById(Long id) {
        for (Flight flight : flights) {
            if (flight.getId() == id) {
                return flight;
            }
        }
        return null;
    }

    void deleteFlightById(Long id) {
        for (Flight flight : flights) {
            if (flight.getId() == id) {
                flights.remove(flight);
                break;
            }
        }
    }

    List<Flight> findFromTo(String from, String to) {
        List<Flight> foundTrips = new ArrayList<>();
        if ((from == null) || (to == null)) {
            return foundTrips;
        } else if (((from.isEmpty()) && (to.isEmpty()))) {
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


    public boolean isFlightPresent(AddFlightRequest request) {
        for (Flight flight : flights) {
            if (flight.getFrom().equals(request.getFrom())
                    && flight.getTo().equals(request.getTo())
                    && flight.getCarrier().equals(request.getCarrier())
                    && flight.getDepartureTime().equals(request.getDepartureTime())
                    && flight.getArrivalTime().equals(request.getArrivalTime()))
                return true;
        }
        return false;
    }

    private Flight flightFinder(FindTripRequest request) {
        List<Flight> foundFlight = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getFrom().equals(request.getFrom())
                    && flight.getTo().equals(request.getTo())
                    && flight.getCarrier().equals(request.getCarrier())
                    && flight.getDepartureTime().toLocalDate().equals(request.getDeparture())
                    && flight.getArrivalTime().toLocalDate().equals(request.getArrival())) ;
            foundFlight.add(flight);
            break;
        }
        return foundFlight.get(0);
    }

}

