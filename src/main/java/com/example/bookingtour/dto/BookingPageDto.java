package com.example.bookingtour.dto;

import java.time.LocalDate;

public class BookingPageDto {
    private int customer_id;
    private int tour_id;
    private LocalDate booking_date;
    private int num_guests;
    private double total_price;
    private int payment_status;
    private String special_request;
    private int booking_status;

    public BookingPageDto() {
    }

    public String getSpecial_request() {
        return special_request;
    }

    public void setSpecial_request(String special_request) {
        this.special_request = special_request;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public LocalDate getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(LocalDate booking_date) {
        this.booking_date = booking_date;
    }

    public int getNum_guests() {
        return num_guests;
    }

    public void setNum_guests(int num_guests) {
        this.num_guests = num_guests;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public int getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(int booking_status) {
        this.booking_status = booking_status;
    }

    public BookingPageDto(int booking_status, String special_request, int payment_status, double total_price, int num_guests, LocalDate booking_date, int tour_id, int customer_id) {
        this.booking_status = booking_status;
        this.special_request = special_request;
        this.payment_status = payment_status;
        this.total_price = total_price;
        this.num_guests = num_guests;
        this.booking_date = booking_date;
        this.tour_id = tour_id;
        this.customer_id = customer_id;
    }
}
