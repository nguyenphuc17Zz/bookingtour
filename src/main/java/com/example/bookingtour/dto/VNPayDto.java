package com.example.bookingtour.dto;

import java.io.Serializable;

public class VNPayDto implements Serializable {
    private String status;
    private String message;
    private String URL;

    public VNPayDto() {
    }

    public VNPayDto(String status, String message, String URL) {
        this.status = status;
        this.message = message;
        this.URL = URL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
