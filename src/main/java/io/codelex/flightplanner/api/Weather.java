package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {

    private final String condition;
    private final double temperature;
    private final int precipitation;
    private final double windSpeed;

    @JsonCreator
    public Weather(
            @JsonProperty("condition") String condition,
            @JsonProperty("temperature") double temperature,
            @JsonProperty("totalprecip mm") int precipitation,
            @JsonProperty("maxwind_kph") double windSpeed) {
        this.condition = condition;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
    }

    public String getCondition() {
        return condition;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }
}
