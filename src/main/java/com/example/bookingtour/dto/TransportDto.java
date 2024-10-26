package com.example.bookingtour.dto;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
public class TransportDto {
    private String transportation_type;
    private String departure_location;
    private String arrival_location;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private Double price;

    public TransportDto() {
    }

    public TransportDto(String transportation_type, String departure_location, String arrival_location, LocalDateTime departure_time, LocalDateTime arrival_time, Double price) {
        this.transportation_type = transportation_type;
        this.departure_location = departure_location;
        this.arrival_location = arrival_location;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.price = price;
    }

    public void setTrasportationType(String transportation_type) {
        this.transportation_type = transportation_type;
    }
    public String getTrasportationType() {
        return transportation_type;
    }


    public void setDepartureLocation(String departure_location) {
        this.departure_location = departure_location;
    }

    public String getDepartureLocation() {
        return departure_location;
    }

    public void setArrivalLocation(String arrival_location) {
        this.arrival_location = arrival_location;
    }
    public String getArrivalLocation() {
        return arrival_location;
    }

    public void setDepartureTime(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }
    public LocalDateTime getDepartureTime() {
        return departure_time;
    }

    public void setArrivalTime(LocalDateTime arrival_time) {
        this.arrival_time = departure_time;
    }
    public LocalDateTime getArrivalTime() {
        return arrival_time;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getPrice() {
        return price;
    }
}
