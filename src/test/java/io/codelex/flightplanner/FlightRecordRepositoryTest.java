package io.codelex.flightplanner;

import io.codelex.flightplanner.repository.AirportRecordRepository;
import io.codelex.flightplanner.repository.FlightRecordRepository;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class FlightRecordRepositoryTest {

    @Autowired
    FlightRecordRepository repository;

    @Autowired
    AirportRecordRepository airportRecordRepository;

    @Test
    void search_should_not_return_any_results_when_nothing_matched() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));


        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsFromTo("Moscow", "Stockholm");

        //then
        Assertions.assertTrue(flights.isEmpty());
    }

    @Test
    void search_should_return_flight_when_airports_both_airports_matched() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));

        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsFromTo("RIX", "dxb");

        //then
        Assertions.assertTrue(flights.contains(flight));
    }

    @Test
    void search_should_return_flight_by_city() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));


        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsFromTo(" ri", "");

        //then
        Assertions.assertFalse(flights.isEmpty());
    }

    @Test
    void search_should_return_flight_by_country() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("RIX", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("DXB", "Dubai", "UAE"));


        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsFromTo("lv ", "UaE");

        //then
        Assertions.assertFalse(flights.isEmpty());
    }

    @Test
    void search_should_return_flight_by_partial_parameter_from() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("AAA", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("BBB", "Dubai", "UAE"));


        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsFrom("rig");

        //then
        Assertions.assertFalse(flights.isEmpty());
    }

    @Test
    void search_should_return_flight_by_partial_parameter_to() {
        //given
        AirportRecord RIX = airportRecordRepository.save(new AirportRecord("AAA", "Riga", "LV"));
        AirportRecord DXB = airportRecordRepository.save(new AirportRecord("BBB", "Dubai", "UAE"));


        FlightRecord flight = new FlightRecord();
        flight.setFrom(RIX);
        flight.setTo(DXB);
        flight.setCarrier("Turkish Airlines");
        flight.setDepartureTime(LocalDate.of(2019, 1, 1).atStartOfDay());
        flight.setArrivalTime(LocalDate.of(2019, 1, 2).atStartOfDay());
        repository.save(flight);

        //when
        List<FlightRecord> flights = repository.searchFlightsTo("uae");

        //then
        Assertions.assertFalse(flights.isEmpty());
    }


}
