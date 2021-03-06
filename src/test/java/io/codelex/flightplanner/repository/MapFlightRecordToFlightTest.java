package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class MapFlightRecordToFlightTest {
    MapFlightRecordToFlight toFlight = new MapFlightRecordToFlight();

    @Test
    void should_be_able_to_map_flightRecord_to_Flight() {

        //given
        FlightRecord flight = new FlightRecord();
        flight.setFrom(new AirportRecord("RIX", "Riga", "LV"));
        flight.setTo(new AirportRecord("DXB", "Dubai", "UAE"));
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        flight.setId((long) 1);

        //when
        Flight flight1 = toFlight.apply(flight);

        //then
        Assertions.assertEquals(flight.getId(), flight1.getId());
        Assertions.assertEquals(flight.getFrom().getAirport(), flight1.getFrom().getAirport());
        Assertions.assertEquals(flight.getTo().getAirport(), flight1.getTo().getAirport());
        Assertions.assertEquals(flight.getCarrier(), flight1.getCarrier());
        Assertions.assertEquals(flight.getDepartureTime(), flight1.getDepartureTime());
        Assertions.assertEquals(flight.getArrivalTime(), flight1.getArrivalTime());
    }
}
