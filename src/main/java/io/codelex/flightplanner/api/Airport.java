package io.codelex.flightplanner.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Airport {

    private String country;
    private String city;
    private String airport;

    @JsonCreator
    public Airport(@JsonProperty ("country") String country,
                   @JsonProperty ("city") String city,
                   @JsonProperty ("airport") String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getAirport() {
        return airport;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport1 = (Airport) o;

        return airport.equals(airport1.airport);
    }

    @Override
    public int hashCode() {
        return airport.hashCode();
    }
}
