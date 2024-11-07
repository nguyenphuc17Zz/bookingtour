package com.example.bookingtour.dto;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
public class TransportDto {

    private int transportationId;
    private String transportationType;
    private  String tag;
    private  int seat;
    private boolean status;

    public TransportDto() {
    }

    public TransportDto(int transportationId, String transportationType, String tag ,int seat, boolean status) {
        this.transportationId = transportationId;
        this.transportationType = transportationType;
        this.tag = tag;
        this.seat = seat;
        this.status = status;
    }

    public void setTransportationId(int transportationId) {
        this.transportationId = transportationId;
    }
    public int getTransportationId() {
        return transportationId;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }
    public String getTransportationType() {
        return transportationType;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }

}
