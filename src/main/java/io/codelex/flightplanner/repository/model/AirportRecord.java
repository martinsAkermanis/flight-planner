package io.codelex.flightplanner.repository.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "airports")
public class AirportRecord {

    @Id
    private String airport;
    private String country;
    private String city;

    public AirportRecord() {
    }

    public AirportRecord(String airport, String city, String country) {
        this.airport = airport;
        this.country = country;
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportRecord that = (AirportRecord) o;
        return airport.equals(that.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airport);
    }
}
