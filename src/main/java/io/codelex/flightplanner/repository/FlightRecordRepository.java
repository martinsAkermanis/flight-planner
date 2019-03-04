package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.repository.model.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("select flight from FlightRecord flight where " +
            "flight.from.airport = :from " +
            "and flight.to = :to " +
            "and flight.departureTime >= :departureFrom " +
            "and flight.departureTime <= :departureTo " +
            "and flight.arrivalTime >= :arrivalFrom " +
            "and flight.arrivalTime <= :arrivalTo ")
    List<FlightRecord> findMatching(@Param("from") String from,
                                    @Param("to") String to,
                                    @Param("departureFrom") LocalDateTime departureFrom,
                                    @Param("departureTo") LocalDateTime departureTo,
                                    @Param("arrivalFrom") LocalDateTime arrivalFrom,
                                    @Param("arrivalTo") LocalDateTime arrivalTo);


    @Query("select count (flight) >0 from FlightRecord flight where " +
            " flight.from = :from " +
            "and flight.to = :to " +
            "and flight.departureTime = :departureTime " +
            "and flight.arrivalTime = :arrivalTime " +
            "and flight.carrier = :carrier ")
    boolean isFlightPresent(@Param("from") String from,
                            @Param("to") String to,
                            @Param("departureTime") LocalDateTime departureFrom,
                            @Param("arrivalTime") LocalDateTime arrivalTime,
                            @Param("carrier") String carrier);
}
