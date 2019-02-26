package io.codelex.flightplanner;

import io.codelex.flightplanner.api.FindTripRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
class PublicTripsController {

    @Autowired
    private FlightService service = new FlightService();

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> search(@RequestParam("from") String from, @RequestParam("to") String to) {
        List<Flight> fromTo = service.findFromTo(from, to);
        if (fromTo.isEmpty()) {
            return new ResponseEntity("No flights found", HttpStatus.OK);
        }
        return new ResponseEntity<>(fromTo, HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<List<Flight>> findFlight(@RequestBody FindTripRequest request) {
        if (request.getTo().equals(request.getFrom())) {
            return new ResponseEntity("Departure and Arrival cannot be same", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(service.findFlight(request), HttpStatus.OK);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Long id) {
        Flight response = service.findFlightById(id);
        if (response == null) {
            return new ResponseEntity("No such flight", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}





