package io.codelex.flightplanner.weather;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.codelex.flightplanner.api.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ForecastCache {
    private final WeatherGateway gateway;

    private final Cache<CacheKey, Weather> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .maximumSize(100)
            .build();

    public ForecastCache(WeatherGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<Weather> checkCacheKey(String city, LocalDate date) {
        CacheKey key = new CacheKey(city, date);

        if (cache.getIfPresent(key) != null) {
            return Optional.ofNullable(cache.getIfPresent(key));
        }
        Optional<Weather> response = gateway.fetchForecast(city, date);

        response.ifPresent(it -> cache.put(key, it));
        return response;
    }
}


