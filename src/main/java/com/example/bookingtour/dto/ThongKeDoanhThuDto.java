package com.example.bookingtour.dto;

import java.time.LocalDate;

public class ThongKeDoanhThuDto {
    private String tourType;
    private LocalDate fromDate;
    private LocalDate toDate;

    // Getters và setters
    public String getTourType() {
        return tourType;
    }
    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
