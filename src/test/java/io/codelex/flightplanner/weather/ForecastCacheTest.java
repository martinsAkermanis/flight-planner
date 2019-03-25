package io.codelex.flightplanner.weather;

import io.codelex.flightplanner.api.Weather;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForecastCacheTest {
    private WeatherGateway gateway = mock(WeatherGateway.class);

    private ForecastCache cache = new ForecastCache(gateway);

    private LocalDate defaultDate = LocalDate.of(2019, 1, 1);
    private Weather defaultWeather = new Weather("Snow", 0, 0, 0);


    @Test
    void should_skip_the_cache_if_nothing_present() {
        //given
        when(gateway.fetchForecast("Riga", defaultDate))
                .thenReturn(Optional.ofNullable(defaultWeather));

        //when
        Optional<Weather> weather = cache.fetchForecast("Riga", defaultDate);

        //then
        assertEquals("Snow", weather.get().getCondition());

    }

    @Test
    void should_use_cache_on_repeated_calls() {
        //given
        when(gateway.fetchForecast("Riga", defaultDate))
                .thenReturn(Optional.ofNullable(defaultWeather));

        //when
        cache.fetchForecast("Riga", defaultDate);
        cache.fetchForecast("Riga", defaultDate);

        //then
        verify(gateway, times(1))
                .fetchForecast("Riga", defaultDate);

    }

}