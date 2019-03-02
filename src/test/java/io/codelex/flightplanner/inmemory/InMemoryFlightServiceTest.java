package io.codelex.flightplanner.inmemory;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.Flight;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFlightServiceTest {
    private InMemoryFlightService service = new InMemoryFlightService();

    @Test
    void should_be_able_to_add_flight() {
        // given
        AddFlightRequest request = addRequest();

        // when
        Flight flight = service.addFlight(request);

        // then
        assertEquals(flight.getFrom(), request.getFrom());
        assertEquals(flight.getTo(), request.getTo());
        assertEquals(flight.getCarrier(), request.getCarrier());
        assertEquals(flight.getDepartureTime(), request.getDepartureTime());
        assertEquals(flight.getArrivalTime(), request.getArrivalTime());
    }

    @Test
    void should_increment_id_when_adding_new_flight() {
        // given
        AddFlightRequest request1 = addRequest();

        AddFlightRequest request2 = new AddFlightRequest(
                new Airport("Russia", "Moscow", "MASC"),
                new Airport("Japan", "Banzai", "DSX"),
                "CoolAir",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        // when
        Flight firstFlight = service.addFlight(request1);
        Flight secondFlight = service.addFlight(request2);

        // then
        assertEquals(firstFlight.getId() + 1, secondFlight.getId());
    }

    @Test
    void should_be_able_to_get_added_flight_by_id() {
        //given
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        Optional<Flight> result = service.findFlightById(flight.getId());

        //then
        assertNotNull(result);
        // and
        assertEquals(flight.getFrom(), request.getFrom());
        assertEquals(flight.getTo(), request.getTo());
        assertEquals(flight.getCarrier(), request.getCarrier());
        assertEquals(flight.getDepartureTime(), request.getDepartureTime());
        assertEquals(flight.getArrivalTime(), request.getArrivalTime());
    }

    @Test
    void should_not_be_able_to_add_duplicated_flight() {
        //given
        AddFlightRequest request = addRequest();

        //when
        service.addFlight(request);

        // then
        assertThrows(
                IllegalStateException.class,
                () -> service.addFlight(request)
        );
    }

    @Test
    void should_be_able_to_delete_flight_by_id() {
        //given
        AddFlightRequest request = addRequest();

        //when
        Optional<Flight> flight = Optional.ofNullable(service.addFlight(request));
        service.deleteFlightById(flight.get().getId());

        //then
        flight = service.findFlightById(flight.get().getId());
        assertNull(flight);
    }

    @Test
    void should_be_able_to_delete_all_flights() {
        //given
        AddFlightRequest request1 = addRequest();
        AddFlightRequest request2 = addRequest2();

        //when
        Optional<Flight> flight1 = Optional.ofNullable(service.addFlight(request1));
        Optional<Flight> flight2 = Optional.ofNullable(service.addFlight(request2));
        service.clearAllFlights();

        //then
        flight1 = service.findFlightById(flight1.get().getId());
        flight2 = service.findFlightById(flight2.get().getId());
        assertNull(flight1);
        assertNull(flight2);
    }

    @Test
    void should_not_be_able_to_find_flight_when_nulls_are_passed() {
        //given
        AddFlightRequest request = addRequest();

        //when
        service.addFlight(request);
        List<Flight> flights = service.search(null, null);

        //then
        assertTrue(flights.isEmpty());

    }

    @Test
    void should_find_flight_where_partial_airport_name_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "ARN");

        //then
        assertTrue(flights.contains(flight));

    }

    @Test
    void should_find_flight_where_partial_country_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "swe");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_city_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "Stoc");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_lowercase_airport_name_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "stoc");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_uppercase_country_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "SWE");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_uppercase_city_from_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("", "STO");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_airport_name_from_with_space_at_the_end_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("rix ", "");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_country_from_with_space_at_the_end_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("LAT   ", "");

        //then
        assertTrue(flights.contains(flight));
    }

    @Test
    void should_find_flight_where_partial_city_from_with_space_at_the_end_passed() {
        AddFlightRequest request = addRequest();

        //when
        Flight flight = service.addFlight(request);
        List<Flight> flights = service.search("   RI    ", "");

        //then
        assertTrue(flights.contains(flight));
    }


    ////////////////////   Sample flight requests   ////////////////////

    private AddFlightRequest addRequest() {
        AddFlightRequest request = new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Rayanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        return request;
    }

    private AddFlightRequest addRequest2() {
        AddFlightRequest request2 = new AddFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "AeroFlot",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        return request2;
    }

}