package io.codelex.flightplanner;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.flightplanner.api.AddTripRequest;
import io.codelex.flightplanner.api.Trip;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class TripService {

    private final List<Trip> trips = new ArrayList<>();
    private Long sequence = 1L;

    Trip addTrip(AddTripRequest request) {
        Trip trip = new Trip(
                1,
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getArrivalTime(),
                request.getDepartureTime()
        );

        trips.add(trip);
        return trip;
    }

    Trip search(String from, String to) {
        return null;
    }

    void deleteTripById(Long id){
        trips.removeIf(trip -> id.equals(trip.getId()));
    }

    void clearAllTrips(){
        trips.clear();
    }

    public List<Trip> findAll() {
        return trips;
    }

    public Optional<Trip> findTripById(Long id) {
        return trips.stream()
                .filter(trip -> id.equals(trip.getId()))
                .findFirst();

    }

    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        builder.modules(javaTimeModule);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return builder;
    }
}


