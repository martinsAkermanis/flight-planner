package io.codelex.flightplanner.weather;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.codelex.flightplanner.api.Weather;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherGatewayTest {

    @Rule
    WireMockRule wireMock = new WireMockRule();
    private WeatherGateway gateway;

    private LocalDate date = LocalDate.of(2019, 3, 23);

    @AfterEach
    void tearDown() {
        wireMock.stop();
    }
    @BeforeEach
    void setUp() {
        wireMock.start();

        ApixuProperties props = new ApixuProperties();
        props.setApiUrl("http://localhost:" + wireMock.port());
        props.setApiKey("123");
        gateway = new WeatherGateway(props);

    }

    @Test
    void should_fetch_forecast() throws Exception {

        //given
        File file = ResourceUtils.getFile((this.getClass().getResource("/stubs/successful-response.json")));
        assertTrue(file.exists());

        byte[] json = Files.readAllBytes(file.toPath());

        wireMock.stubFor(get(urlPathEqualTo("/v1/forecast.json"))
                .withQueryParam("key", equalTo("123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(200)
                        .withBody(json)));

        //when
        Optional<Weather> weather = gateway.fetchForecast("Riga", date);

        //then
        assertEquals("Moderate or heavy rain shower", weather.get().getCondition());
        assertEquals(23.8, weather.get().getWindSpeed());
        assertEquals(4.6, weather.get().getTemperature());
        assertEquals(1, weather.get().getPrecipitation());

    }

    @Test
    void should_handle_external_service_failure() {

        wireMock.stubFor(get(urlPathEqualTo("/v1/forecast.json"))
                .withQueryParam("key", equalTo("123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(500)));

        //when
        Optional<Weather> response = gateway.fetchForecast("Riga", date);

        //then
        assertFalse(response.isPresent());
    }
}