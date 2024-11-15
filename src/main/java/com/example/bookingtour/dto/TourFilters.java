package com.example.bookingtour.dto;

public class TourFilters {
    private String tourType;
    private Integer rating;
    private Integer availableSeats;
    private String startDate;
    private String endDate;

    public TourFilters(String tourType, Integer rating, Integer availableSeats, String startDate, String endDate) {
        this.tourType = tourType;
        this.rating = rating;
        this.availableSeats = availableSeats;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
