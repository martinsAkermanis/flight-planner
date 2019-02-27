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

    Flight addFlight(AddFlightRequest request) {
        long flightId = id.incrementAndGet();

        Flight flight = new Flight(
                flightId,
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDepartureTime(),
                request.getArrivalTime()
        );
        if (isFlightPresent(request)
                || (request.getArrivalTime().isEqual(request.getDepartureTime()))) {
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
            if (flight.getFrom().getAirport().toLowerCase().equals(request.getFrom().getAirport().toLowerCase())
                    && flight.getTo().getAirport().toLowerCase().equals(request.getTo().getAirport().toLowerCase())
                    && flight.getCarrier().toLowerCase().equals(request.getCarrier().toLowerCase())
                    && flight.getDepartureTime().equals(request.getDepartureTime())
                    && flight.getArrivalTime().equals(request.getArrivalTime())) {
                return true;
            }
        }
        return false;
    }

    public List<Flight> flightFinder(FindFlightRequest request) {
        List<Flight> foundFlight = new ArrayList<>();

        for (Flight flight : flights) {
            if ((flight.getFrom().equals(request.getFrom()))
                    && (flight.getTo().equals(request.getTo()))
                    /*&& (flight.getDepartureTime().toLocalDate().isEqual(request.getDeparture()))
                    && (flight.getArrivalTime().toLocalDate().isEqual(request.getArrival())))*/
                    && flight.getDepartureTime().withHour(0).withMinute(0).withSecond(0).withNano(0).isEqual(request.getDeparture().atStartOfDay())
                    && flight.getArrivalTime().withHour(0).withMinute(0).withSecond(0).withNano(0).isEqual(request.getArrival().atStartOfDay()))
                    {
                foundFlight.add(flight);
               // break;
            }
        }
        return foundFlight;
    }

    public Long getFlightId(FindFlightRequest request) {
        Long foundFlightId = null;
        for (Flight flight : flights) {
            if ((flight.getFrom().equals(request.getFrom()))
                    && (flight.getTo().equals(request.getTo()))
                    && (flight.getDepartureTime().toLocalDate().equals(request.getDeparture()))
                    && (flight.getArrivalTime().toLocalDate().equals(request.getArrival()))) {
                foundFlightId = flight.getId();
                break;
            }
        }
        return foundFlightId;
    }
}


