package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.repository.model.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRecordRepository extends JpaRepository<FlightRecord, Long> {

    @Query("select flight from FlightRecord flight where " +
            " lower(flight.from.airport) like lower(concat('%', :from, '%'))" +
            " or lower(flight.to.airport) like lower(concat('%', :to, '%'))" +
            " or lower(flight.from.city) like lower(concat('%', :from, '%'))" +
            " or lower(flight.to.city) like lower(concat('%', :to, '%'))" +
            " or lower(flight.to.country) like lower(concat('%', :from, '%'))" +
            " or lower(flight.to.country) like lower(concat('%', :to, '%'))")

    List<FlightRecord> searchFlights(@Param("from") String from, @Param("to") String to);
}
