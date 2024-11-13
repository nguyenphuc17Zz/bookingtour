package com.example.bookingtour.entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;
    private int tour_id;
    private int customer_id;
    private int rating;
    private String review_comment;
    private LocalDate review_date;
    private int status;


    public Review() {
    }

    public Review(int review_id, int tour_id, int customer_id, int rating, String review_comment, LocalDate review_date, int status){
    }
    public void setReviewId(int review_id) {
        this.review_id = review_id;
    }
    public int getReviewId() {
        return review_id;
    }
    public void setTourId(int tour_id) {
        this.tour_id = tour_id;
    }
    public int getTourId() {
        return tour_id;
    }
    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
    public int getCustomerId() {
        return customer_id;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getRating() {
        return rating;
    }

    public void setReviewComment(String review_comment) {
        this.review_comment = review_comment;
    }
    public String getReviewComment() {
        return review_comment;
    }

    public void setReviewDate(LocalDate review_date) {
        this.review_date = review_date;
    }
    public LocalDate getReviewDate() {
        return review_date;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}


