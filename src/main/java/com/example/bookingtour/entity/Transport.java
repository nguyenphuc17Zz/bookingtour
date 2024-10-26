package com.example.bookingtour.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "transportation")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transportation_id;
    private String transportation_type;
    private  String departure_location;
    private  String arrival_location;
    private  LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private Double price;


    public Transport(String transportation_type, String departure_location, String arrival_location, LocalDateTime departure_time, LocalDateTime arrival_time, Double price) {
        this.transportation_type = transportation_type;
        this.departure_location = departure_location;
        this.arrival_location = arrival_location;
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
        this.price = price;
    }
}
