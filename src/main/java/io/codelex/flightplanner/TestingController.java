package io.codelex.flightplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingController {

    @Autowired
    private FlightService service;

    @PostMapping("/clear")
    public ResponseEntity clearAllTrips() {
        service.clearAllFlights();
        return new ResponseEntity("All trips deleted!", HttpStatus.OK);
    }

}
