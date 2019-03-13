package io.codelex.flightplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingController {

    @Autowired
    private FlightService service;

    @PostMapping("/clear")
    public void clearAllFlights() {
        service.clearAllFlights();
    }
}
