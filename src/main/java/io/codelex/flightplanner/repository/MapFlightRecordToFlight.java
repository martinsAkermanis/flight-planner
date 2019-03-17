package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.FlightRecord;

import java.util.function.Function;

public class MapFlightRecordToFlight implements Function<FlightRecord, Flight> {


    @Override
    public Flight apply(FlightRecord flightRecord) {
        return new Flight(
                flightRecord.getId(),
                new Airport(
                        flightRecord.getFrom().getAirport(),
                        flightRecord.getFrom().getCity(),
                        flightRecord.getFrom().getCountry()
                ),
                new Airport(
                        flightRecord.getTo().getAirport(),
                        flightRecord.getTo().getCity(),
                        flightRecord.getTo().getCountry()
                ),
                flightRecord.getCarrier(),
                flightRecord.getDepartureTime(),
                flightRecord.getArrivalTime()
        );
    }
}
