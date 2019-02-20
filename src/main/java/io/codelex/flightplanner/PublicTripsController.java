package io.codelex.flightplanner;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindTripRequest;
import io.codelex.flightplanner.api.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
class PublicTripsController {

    @Autowired
    private TripService tripService = new TripService();

    @GetMapping("/flights/search")
    public List<Trip> search(String from, String to) {
        return tripService.findAll();
    }

    @PostMapping("/flights")
    public ResponseEntity<List<Trip>> findTrip(@RequestBody FindTripRequest request) {
        System.out.println(request.getFrom());
        List<Trip> trips = new ArrayList<>();

        Trip trip2 = new Trip(
                4L,
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                "Norwegian", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        trips.add(trip2);
        return new ResponseEntity<>(trips, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Trip> findTripById(@PathVariable Long id) {
        return tripService.findTripById(id)
                .map(trip -> new ResponseEntity<>(trip,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}




