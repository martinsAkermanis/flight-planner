package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/internal-api")
class InternalTripsController {

    @Autowired
    private FlightService service;

    @PutMapping("/flights")
    public ResponseEntity addTrip(@Valid @RequestBody AddFlightRequest request) {
        if (isRequestNull(request) || areValuesSame(request)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (request.getArrivalTime().equals(request.getDepartureTime())
                || request.getArrivalTime().isBefore(request.getDepartureTime())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (service.isFlightPresent(request)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(service.addFlight(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity deleteTripById(@PathVariable("id") Long id) {
        service.deleteFlightById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity findFlightById(@PathVariable Long id) {
        return getResponseEntity(id, service);
    }

    static ResponseEntity getResponseEntity(@PathVariable Long id, FlightService service) {
        Flight response = service.findFlightById(id);
        if (response == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isRequestNull(AddFlightRequest request) {
        if ((request.getFrom() == null || request.getTo() == null)) {
            return true;
        }
        if ((request.getFrom().getCountry() == null
                || request.getFrom().getCity() == null
                || request.getFrom().getAirport() == null)) {
            return true;
        }
        if ((request.getTo().getCountry() == null
                || request.getTo().getCity() == null
                || request.getTo().getAirport() == null)) {
            return true;
        }
        if ((request.getCarrier() == null) || (request.getCarrier().equals(""))) {
            return true;
        }
        if (request.getDepartureTime() == null || request.getArrivalTime() == null) {
            return true;
        }
        if ((request.getFrom().getCountry().equals("")
                || request.getFrom().getCity().equals("")
                || request.getFrom().getAirport().equals(""))) {
            return true;
        }
        return (request.getTo().getCountry().equals("")
                || request.getTo().getCity().equals("")
                || request.getTo().getAirport().equals(""));
    }

    private boolean areValuesSame(AddFlightRequest request) {
        return ((request.getTo().equals(request.getFrom())
                || (request.getFrom().getCity().toLowerCase().equals(request.getTo().getCity().toLowerCase())))
                || (request.getFrom().getAirport().toLowerCase().equals(request.getTo().getAirport().toLowerCase())));
    }

}
