package com.example.bookingtour.dto;

import java.time.LocalDate;

public class BookingDto {
    private int booking_id;
    private int customer_id;
    private int tour_id;
    private int transportation_id;
    private LocalDate booking_date;
    private int num_guests;
    private Double total_price;
    private String payment_status;
    private String special_requests;
    private String booking_status;

    public BookingDto() {
    }

    public BookingDto(int booking_id, int customer_id, int tour_id, int transportation_id_id, LocalDate booking_date, int num_guests, Double total_price, String payment_status, String special_requests, String booking_status){
    }

    public void setBookingId(int booking_id) {
        this.booking_id = booking_id;
    }
    public int getBookingId() {
        return booking_id;
    }
    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
    public int getCustomerId() {
        return customer_id;
    }
    public void setTourId(int tour_id) {
        this.tour_id = tour_id;
    }
    public int getTourId() {
        return tour_id;
    }
    public void setTransportationId(int transportation_id) {
        this.transportation_id = transportation_id;
    }
    public int getTransportationId() {
        return transportation_id;
    }

    public void setBookingDate(LocalDate booking_date) {
        this.booking_date = booking_date;
    }
    public LocalDate getBookingDate() {
        return booking_date;
    }

    public void setNumGuests(int num_guests) {
        this.num_guests = num_guests;
    }
    public int getNumGuests() {
        return num_guests;
    }
    public void setTotalPrice(Double total_price) {
        this.total_price = total_price;
    }
    public Double getTotalPrice() {
        return total_price;
    }

    public void setPaymentStatus(String payment_status) {
        this.payment_status = payment_status;
    }
    public String getPaymentStatus() {
        return payment_status;
    }

    public void setSpecialRequest(String special_requests) {
        this.special_requests = special_requests;
    }
    public String getSpecialRequest() {
        return special_requests;
    }

    public void setBookingStatus(String booking_status) {
        this.booking_status = booking_status;
    }
    public String getBookingStatus() {
        return booking_status;
    }
}
