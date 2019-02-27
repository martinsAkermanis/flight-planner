package io.codelex.flightplanner;

import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
class PublicTripsController {

    @Autowired
    private FlightService service = new FlightService();

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> search(@RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {
        List<Flight> fromTo = service.findFromTo(from, to);
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
        if ((request.getFrom() == null || request.getTo() == null)
                || (request.getFrom().getCountry() == null || request.getFrom().getCity() == null || request.getFrom().getAirport() == null)
                || (request.getTo().getCountry() == null || request.getTo().getCity() == null || request.getTo().getAirport() == null)
                || request.getDeparture() == null
                || request.getArrival() == null
                || (request.getFrom().getCountry().equals("") || request.getFrom().getCity().equals("") || request.getFrom().getAirport().equals(""))
                || (request.getTo().getCountry().equals("") || request.getTo().getCity().equals("") || request.getTo().getAirport().equals(""))) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (request.getTo().equals(request.getFrom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(service.flightFinder(request));
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity findFlightById(@PathVariable Long id) {
        Flight response = service.findFlightById(id);
        if (response == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*protected Flight getFlightData(FindFlightRequest request) {
        return new Flight(
                service.getFlightId(request),
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getArrival().atStartOfDay(),
                request.getDeparture().atStartOfDay());
    }*/

}





