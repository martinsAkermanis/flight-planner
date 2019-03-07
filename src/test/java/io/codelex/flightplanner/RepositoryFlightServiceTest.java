package io.codelex.flightplanner;

import io.codelex.flightplanner.repository.AirportRecordRepository;
import io.codelex.flightplanner.repository.FlightRecordRepository;
import io.codelex.flightplanner.repository.RepositoryFlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


class RepositoryFlightServiceTest {

    @Autowired
    RepositoryFlightService repositoryFlightService;
    @Autowired
    AirportRecordRepository airportRecordRepository;
    @Autowired
    FlightRecordRepository flightRecordRepository;

    private MockMvc mockMvc;

    @Test
    void should_be_able_to_map_flightRecord_to_Flight() {
        //given

        //when


        //then
    }

}
