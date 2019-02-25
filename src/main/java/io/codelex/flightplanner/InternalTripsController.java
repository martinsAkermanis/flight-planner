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
    public ResponseEntity<Flight> addTrip(@RequestBody AddFlightRequest request) {
        if (request == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else if ((request.getTo().equals(request.getFrom())
                || (request.getFrom().getCity().equals(request.getTo().getCity())))
                || (request.getFrom().getAirport().equals(request.getTo().getAirport()))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.addFlight(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity deleteTripById(@PathVariable("id") Long id) {
        Flight response = service.findFlightById(id);
        if (response == null) {
            return new ResponseEntity("No such flight", HttpStatus.BAD_REQUEST);
        }

        service.deleteFlightById(id);
        return new ResponseEntity("Flight deleted successfully", HttpStatus.OK);


        /*}
        return new ResponseEntity("No such flight to delete", HttpStatus.BAD_REQUEST);*/

    }

}
