package io.codelex.flightplanner.api;

public class Weather {

    private final String condition;
    private final double temperature;
    private final int precipitation;
    private final double windSpeed;


    public Weather(
            String condition,
            double temperature,
            int precipitation,
            double windSpeed) {
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
