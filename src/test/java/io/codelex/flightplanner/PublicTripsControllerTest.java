package io.codelex.flightplanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindTripRequest;
import io.codelex.flightplanner.api.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(PublicTripsController.class)
class PublicTripsControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
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
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.registerModule(javaTimeModule);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService service;

    @Test
    void should_get_400_when_from_and_to_are_equal() throws Exception {
        //given
        FindTripRequest request = new FindTripRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Latvia", "Riga", "RIX"),
                LocalDate.now(),
                LocalDate.now());
        String json = MAPPER.writeValueAsString(request);

        //expect
        mockMvc.perform(
                post("/api/flights")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_200_and_find_flights() throws Exception {
        //given
        FindTripRequest request = new FindTripRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                LocalDate.now(),
                LocalDate.now());
        String jsonRequest = MAPPER.writeValueAsString(request);

        Flight flight = new Flight(
                1L,
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDeparture().atStartOfDay(),
                request.getArrival().atStartOfDay()
        );
        /*Mockito.lenient()
                .when(service.findFlight(any()))
                .thenReturn(Collections.singletonList(flight));*/

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/flights")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Flight> flights = MAPPER.readValue(
                jsonResponse, new TypeReference<List<Flight>>() {
                }
        );
        Assertions.assertFalse(flights.isEmpty());
    }

    @Test
    void should_get_200_when_choosing_from_and_to() throws Exception {
        //given
        FindTripRequest request = new FindTripRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "ARN"),
                LocalDate.now(),
                LocalDate.now());
        String jsonRequest = MAPPER.writeValueAsString(request);

        Flight flight = new Flight(
                1L,
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDeparture().atStartOfDay(),
                request.getArrival().atStartOfDay()
        );
        /*Mockito.lenient()
                .when(service.findFlight(any()))
                .thenReturn(Collections.singletonList(flight));*/

        //expect
        String jsonResponse = mockMvc.perform(
                get("/api/flights/search?from=Riga&to=Sto")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Flight> flights = MAPPER.readValue(
                jsonResponse, new TypeReference<List<Flight>>() {
                }
        );
        Assertions.assertFalse(flights.isEmpty());
    }

    @Test
    void should_be_able_to_delete_all_flights() throws Exception {
        //expect
        mockMvc.perform(
                post("/testing-api/clear"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

