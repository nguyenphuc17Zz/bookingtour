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
    private String tag;
    private int seat;
    private int status;

    public Transport() {
    }

    public Transport(int transportation_id, String transportation_type, String tag ,int seat, int status) {
        this.transportation_id = transportation_id;
        this.transportation_type = transportation_type;
        this.tag = tag;
        this.seat = seat;
        this.status = status;
    }

    public void setTransportationId(int transportation_id) {
        this.transportation_id = transportation_id;
    }
    public int getTransportationId() {
        return transportation_id;
    }

    public void setTransportationType(String transportation_type) {
        this.transportation_type = transportation_type;
    }
    public String getTransportationType() {
        return transportation_type;
    }

    public void setTag(String tag) {



        this.tag = tag.toUpperCase();



    }
    public String getTag() {
        return tag;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
    public int getSeat() {
        return seat;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }


}



