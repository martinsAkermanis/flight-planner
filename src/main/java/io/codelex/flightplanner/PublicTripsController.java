package io.codelex.flightplanner;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static io.codelex.flightplanner.InternalTripsController.getResponseEntity;

@RestController
@RequestMapping("/api")
class PublicTripsController {

    @Autowired
    private FlightService service;

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> search(@RequestParam(value = "from", required = false) String from,
                                               @RequestParam(value = "to", required = false) String to) {
        List<Flight> fromTo = service.search(from, to);
        if (from == null && to == null) {
            if (fromTo.isEmpty()) {
                return new ResponseEntity<>(service.getAllFlights(), HttpStatus.OK);
            }
            return new ResponseEntity<>(service.getAllFlights(), HttpStatus.OK);
        }
        return new ResponseEntity<>(fromTo, HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<List<Flight>> findFlight(@Valid @RequestBody FindFlightRequest request) {
        if (((request.getFrom() == null || request.getTo() == null))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAnyAirportFieldNull(request.getFrom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAnyAirportFieldNull(request.getTo())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAnyDepartureNull(request)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAnyAirportFieldEmpty(request.getFrom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (isAnyAirportFieldEmpty(request.getTo())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getTo().equals(request.getFrom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.findFlight(request), HttpStatus.OK);
    }

    private boolean isAnyAirportFieldNull(Airport airport) {
        return airport.getCountry() == null
                || airport.getCity() == null
                || airport.getAirport() == null;
    }

    private boolean isAnyAirportFieldEmpty(Airport airport) {
        return airport.getCountry().equals("")
                || airport.getCity().equals("")
                || airport.getAirport().equals("");
    }

    private boolean isAnyDepartureNull(@RequestBody @Valid FindFlightRequest request) {
        return request.getDeparture() == null
                || request.getArrival() == null;
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity findFlightById(@PathVariable Long id) {
        return getResponseEntity(id, service);
    }
}





