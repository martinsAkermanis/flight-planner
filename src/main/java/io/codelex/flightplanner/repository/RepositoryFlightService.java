package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.FlightService;
import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public class RepositoryFlightService implements FlightService {
    private final FlightRecordRepository flightRecordRepository;
    private final AirportRecordRepository airportRecordRepository;
    private final MapFlightRecordToFlight toFlight = new MapFlightRecordToFlight();

    public RepositoryFlightService(FlightRecordRepository flightRecordRepository,
                                   AirportRecordRepository airportRecordRepository) {
        this.flightRecordRepository = flightRecordRepository;
        this.airportRecordRepository = airportRecordRepository;
    }

    @Override
    public Flight addFlight(AddFlightRequest request) {
        if (isFlightPresent(request)) {
            return null;
        }

        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFrom(createOrGetAirport(request.getFrom()));
        flightRecord.setTo(createOrGetAirport(request.getTo()));
        flightRecord.setCarrier(request.getCarrier());
        flightRecord.setDepartureTime(request.getArrivalTime());
        flightRecord.setArrivalTime(request.getArrivalTime());

        flightRecordRepository.save(flightRecord);


        return flightRecordRepository.findById(flightRecord.getId()).map(toFlight).orElse(null);
    }

    @Override
    public List<Flight> search(String from, String to) {

        return flightRecordRepository.searchFlights(from, to)
                .stream()
                .map(flightRecord -> toFlight.apply(flightRecord))
                .collect(Collectors.toList());

    }

    @Override
    public Flight findFlightById(Long id) {
        return flightRecordRepository.findById(id)
                .map(toFlight)
                .orElse(null);
    }

    @Override
    public void deleteFlightById(Long id) {
        flightRecordRepository.deleteById(id);
    }

    @Override
    public void clearAllFlights() {
        flightRecordRepository.deleteAll();
    }

    @Override
    public boolean isFlightPresent(AddFlightRequest request) {
        return flightRecordRepository.isFlightPresent(
                request.getFrom().getAirport(),
                request.getTo().getAirport(),
                request.getDepartureTime(),
                request.getArrivalTime(),
                request.getCarrier());
    }

    @Override
    public List<Flight> getAllFlights() {
        new ArrayList<>(flightRecordRepository.findAll());
        return null;
    }

    @Override
    public List<Flight> findFlight(FindFlightRequest request) {
        return flightRecordRepository.findMatching(
                request.getFrom().getAirport(),
                request.getTo().getAirport(),
                request.getDeparture().atStartOfDay(),
                request.getDeparture().atStartOfDay().plusDays(1),
                request.getArrival().atStartOfDay(),
                request.getArrival().atStartOfDay().plusDays(1))
                .stream()
                .map(toFlight)
                .collect(Collectors.toList());
    }

    private AirportRecord createOrGetAirport(Airport airport) {
        return airportRecordRepository.findById(airport.getAirport())
                .orElseGet(() -> {
                    AirportRecord created = new AirportRecord(
                            airport.getAirport(),
                            airport.getCity(),
                            airport.getCountry()
                    );
                    return airportRecordRepository.save(created);
                });
    }

}
