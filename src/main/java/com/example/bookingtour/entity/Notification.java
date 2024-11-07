package com.example.bookingtour.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notification_id;
    private int customer_id;
    private String message;
    private LocalDate send_date;
    private String status;

    public Notification() {
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getSend_date() {
        return send_date;
    }

    public void setSend_date(LocalDate send_date) {
        this.send_date = send_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Notification(int notification_id, int customer_id, String message, LocalDate send_date, String status) {
        this.notification_id = notification_id;
        this.customer_id = customer_id;
        this.message = message;
        this.send_date = send_date;
        this.status = status;
    }
}
