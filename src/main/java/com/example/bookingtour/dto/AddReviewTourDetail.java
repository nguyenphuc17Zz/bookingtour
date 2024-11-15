package com.example.bookingtour.dto;

import java.time.LocalDate;
import java.util.Date;

public class AddReviewTourDetail {
    private int tour_id;
    private int customer_id;
    private int rating;
    private String review_comment;
    private LocalDate review_date;
    private int status;

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public LocalDate getReview_date() {
        return review_date;
    }

    public void setReview_date(LocalDate review_date) {
        this.review_date = review_date;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
