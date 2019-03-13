package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RepositoryFlightServiceTest {

    RepositoryFlightService repositoryFlightService;

    @Mock
    FlightRecordRepository flightRecordRepository;

    @Mock
    AirportRecordRepository airportRecordRepository;
    MapFlightRecordToFlight mapFlightRecordToFlight = new MapFlightRecordToFlight();

    FlightRecord flightToTest = newFlightRecord();
    final Long flightId = 1L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        repositoryFlightService = new RepositoryFlightService(flightRecordRepository, airportRecordRepository);
        flightToTest.setId(flightId);
    }

    @Test
    void add_Flight() {
        List<FlightRecord> flightRecordList = new ArrayList<>();

        flightRecordList.add(flightToTest);

        when(flightRecordRepository.findAll()).thenReturn((flightRecordList));
        assertEquals(flightRecordList.size(), 1);

    }

    @Test
    void search() {
        List<Flight> flightList = new ArrayList<>();

        Flight flight = mapFlightRecordToFlight.apply(flightToTest);
        flightList.add(flight);

        String from = "Latvia";
        String to = "UAE";

        when(repositoryFlightService.search(from, to))
                .thenReturn((flightList));

        assertEquals(flight.getFrom().getAirport(), from);
        assertEquals(flight.getTo().getAirport(), to);
    }

    @Test
    void findFlightById() {

        when(flightRecordRepository.findById(flightId)).thenReturn(java.util.Optional.ofNullable(flightToTest));
        Optional<FlightRecord> flightRecord = flightRecordRepository.findById(flightId);

        assertEquals(flightId, flightRecord.get().getId());

    }

    @Test
    void deleteFlightById() {

        when(flightRecordRepository.findById(flightId)).thenReturn(java.util.Optional.ofNullable(flightToTest));
        Optional<FlightRecord> flightRecord = flightRecordRepository.findById(flightId);

        assertEquals(flightId, flightRecord.get().getId());
    }

    @Test
    void clearAllFlights() {

        flightRecordRepository.save(flightToTest);
        flightRecordRepository.deleteAll();

        assertTrue(flightRecordRepository.count() == 0);
    }

    @Test
    void getAllFlights() {
        List<FlightRecord> flightRecordList = new ArrayList<>();

        flightRecordList.add(flightToTest);

        when(flightRecordRepository.findAll()).thenReturn(flightRecordList);

        assertTrue(flightRecordList.contains(flightToTest));
    }


    private AddFlightRequest newFlightRequest() {
        return new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("UAE", "Dubai", "DXB"),
                "Turkish",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
    }

    private FlightRecord newFlightRecord() {

        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setId(flightId);
        flightRecord.setFrom(new AirportRecord("Latvia", "Riga", "RIX"));
        flightRecord.setTo(new AirportRecord("UAE", "Dubai", "DXB"));
        flightRecord.setCarrier("Turkish");
        flightRecord.setDepartureTime(LocalDateTime.now());
        flightRecord.setArrivalTime(LocalDateTime.now().plusHours(1));

        return flightRecord;
    }

}