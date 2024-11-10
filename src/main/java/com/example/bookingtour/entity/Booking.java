package com.example.bookingtour.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int booking_id;
    private int customer_id;
    private int tour_id;
    private Integer transportation_id;
    private LocalDate booking_date;
    private int num_guests;
    private Double total_price;
    private int payment_status;
    private String special_requests;
    private int booking_status;


    public Booking() {
    }

    public Booking(int booking_id, int customer_id, int tour_id, int transportation_id, LocalDate booking_date, int num_guests, Double total_price, int payment_status, String special_requests, int booking_status){
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
    public void setTransportationId(Integer transportation_id) {
        this.transportation_id = transportation_id;
    }
    public Integer getTransportationId() {
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

    public void setPaymentStatus(int payment_status) {
        this.payment_status = payment_status;
    }
    public int getPaymentStatus() {
        return payment_status;
    }

    public void setSpecialRequest(String special_requests) {
        this.special_requests = special_requests;
    }
    public String getSpecialRequest() {
        return special_requests;
    }

    public void setBookingStatus(int booking_status) {
        this.booking_status = booking_status;
    }
    public int getBookingStatus() {
        return booking_status;
    }
}
