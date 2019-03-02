package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.repository.model.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightRecordRepository extends JpaRepository<FlightRecord, Long> {

    @Query("select flight from FlightRecord flight")
    List<FlightRecord> searchFlights(String from, String to);
}
