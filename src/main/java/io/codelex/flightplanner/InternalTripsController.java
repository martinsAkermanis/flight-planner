package io.codelex.flightplanner;

import io.codelex.flightplanner.api.AddFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal-api")
class InternalTripsController {

    @Autowired
    private FlightService service;

    @PutMapping("/flights")
    public ResponseEntity addTrip(@RequestBody AddFlightRequest request) {
        if (service.isFlightPresent(request)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (isRequestNull(request)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (areValuesSame(request)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if ((request.getArrivalTime().equals(request.getDepartureTime()))
                || (request.getArrivalTime().isBefore(request.getDepartureTime()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.addFlight(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity deleteTripById(@PathVariable("id") Long id) {
        if (service.findFlightById(id) == null) {
            return new ResponseEntity<>(service.findFlightById(id), HttpStatus.OK);
        }
        service.deleteFlightById(id);
        return new ResponseEntity<>("Flight deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity findFlightById(@PathVariable Long id) {
        Flight response = service.findFlightById(id);
        if (response == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isRequestNull(AddFlightRequest request) {
        if ((request.getFrom() == null || request.getTo() == null)
                || (request.getFrom().getCountry() == null || request.getFrom().getCity() == null || request.getFrom().getAirport() == null)
                || (request.getTo().getCountry() == null || request.getTo().getCity() == null || request.getTo().getAirport() == null)
                || (request.getCarrier() == null) || (request.getCarrier().equals(""))
                || request.getDepartureTime() == null
                || request.getArrivalTime() == null
                || (request.getFrom().getCountry().equals("") || request.getFrom().getCity().equals("") || request.getFrom().getAirport().equals(""))
                || (request.getTo().getCountry().equals("") || request.getTo().getCity().equals("") || request.getTo().getAirport().equals(""))) {
            return true;
        }
        return false;
    }

    private boolean areValuesSame(AddFlightRequest request) {
        if (((request.getTo().equals(request.getFrom())
                || (request.getFrom().getCity().toLowerCase().equals(request.getTo().getCity().toLowerCase())))
                || (request.getFrom().getAirport().toLowerCase().equals(request.getTo().getAirport().toLowerCase())))) {
            return true;
        }
        return false;
    }

}
