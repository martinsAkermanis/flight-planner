package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.repository.model.AirportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepordRepository extends JpaRepository <AirportRecord, String> {



}
