package io.codelex.flightplanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FlightController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/flights")
    public Flight flightList(@RequestParam(value = "from", defaultValue = "RIX") String name) {
        return new Flight(counter.incrementAndGet(),
                String.format(template, name));
    }

    private String toString(Flight flight1) {
        return toString(flight1);
    }
}

