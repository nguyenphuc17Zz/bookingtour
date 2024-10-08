package com.example.bookingtour.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customer_id;
    private String name;
    private  String email;
    private  String phone_number;
    private  String password;
    private String address;
    private LocalDateTime date_joined;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Customer(String name, String email, String phone_number, String password, String address, LocalDateTime date_joined, boolean status) {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.address = address;
        this.date_joined = date_joined;
        this.status = status;
    }



    public Customer() {
    }

    public Customer(int customer_id, String name, String email, String phone_number, String password, LocalDateTime date_joined, String address) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.date_joined = date_joined;
        this.address = address;
    }

    public Customer(String name, String email, String phone_number, String password, String address, LocalDateTime date_joined) {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.address = address;
        this.date_joined = date_joined;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(LocalDateTime date_joined) {
        this.date_joined = date_joined;
    }
}
