package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.Flight;
import io.codelex.flightplanner.repository.model.AirportRecord;
import io.codelex.flightplanner.repository.model.FlightRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RepositoryFlightServiceTest {

    RepositoryFlightService repositoryFlightService;

    @Mock
    FlightRecordRepository flightRecordRepository;
    @Mock
    AirportRecordRepository airportRecordRepository;

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
        //given
        List<FlightRecord> flightRecordList = new ArrayList<>();
        flightRecordList.add(flightToTest);

        //when
        when(flightRecordRepository.findAll()).thenReturn((flightRecordList));
        Mockito.when(airportRecordRepository.save(Mockito.any()))
                .thenAnswer((Answer) invocation -> invocation.getArguments()[0]);
        when(flightRecordRepository.save(any())).thenReturn(flightToTest);

        //then
        Flight flight = repositoryFlightService.addFlight(newFlightRequest());

        List<Flight> flightList = repositoryFlightService.getAllFlights();

        assertTrue(!flightList.isEmpty());
        assertEquals(flight.getCarrier(), newFlightRequest().getCarrier());
    }


    @Test
    void search() {
        //given
        List<FlightRecord> flightRecordList = new ArrayList<>();
        flightRecordList.add(flightToTest);

        String from = "Latvia";
        String to = "UAE";

        //when
        when(flightRecordRepository.save(any())).thenReturn(flightToTest);
        when(flightRecordRepository.searchFlightsFromTo(from, to)).thenReturn(flightRecordList);
        when(flightRecordRepository.searchFlightsFrom(from)).thenReturn(flightRecordList);
        when(flightRecordRepository.searchFlightsTo(to)).thenReturn(flightRecordList);

        //then
        flightRecordRepository.save(flightToTest);

        List<Flight> searchResultFromTo = repositoryFlightService.search(from, to);
        List<Flight> searchResultFrom = repositoryFlightService.search(from, "");
        List<Flight> searchResultTo = repositoryFlightService.search("", to);
        assertTrue(!searchResultFromTo.isEmpty());
        assertTrue(!searchResultFrom.isEmpty());
        assertTrue(!searchResultTo.isEmpty());
    }

    @Test
    void findFlightById() {

        //when
        when(flightRecordRepository.save(any())).thenReturn(flightToTest);
        when(airportRecordRepository.save(any())).thenReturn(new AirportRecord());
        when(flightRecordRepository.findById(flightId)).thenReturn(Optional.ofNullable(flightToTest));

        //then
        Flight searchResult = repositoryFlightService.findFlightById(1L);

        assertEquals(searchResult.getId(), flightId);
        assertNotNull(flightRecordRepository.findById(flightToTest.getId()));

    }

    @Test
    void deleteFlightById() {

        //when
        when(flightRecordRepository.save(any())).thenReturn(flightToTest);

        //then
        flightRecordRepository.save(flightToTest);
        repositoryFlightService.clearAllFlights();

        assertEquals(flightRecordRepository.count(), 0);
        assertEquals(flightRecordRepository.findById(flightToTest.getId()), Optional.empty());
    }


    @Test
    void getAllFlights() {
        //given
        List<FlightRecord> flightRecordList = new ArrayList<>();
        flightRecordList.add(flightToTest);

        //when
        when(flightRecordRepository.findAll()).thenReturn(flightRecordList);

        //then
        List<Flight> flightList = repositoryFlightService.getAllFlights();
        assertTrue(!flightList.isEmpty());
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
        flightRecord.setDepartureTime(newFlightRequest().getDepartureTime());
        flightRecord.setArrivalTime(newFlightRequest().getArrivalTime());

        return flightRecord;
    }

}