package com.example.bookingtour.controller;

import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.repository.BookingRepository;
import com.example.bookingtour.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

@Controller
public class HistoryController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/history")
    public String showHistory(@RequestParam("id") int id, Model model) {
        List<Object[]> bookings = bookingService.getAllBookingsByUserId(id);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        for (Object[] booking : bookings) {
            if (booking[5] instanceof Double || booking[5] instanceof Long) {
                booking[5] = decimalFormat.format(booking[5]) + " VND";
            }
        }

        model.addAttribute("bookings", bookings);
        return "user/history/history";
    }
    public static String formatToVND(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(amount) + " VND";
    }
    @PutMapping("history/cancel/{bookingId}")
    @ResponseBody
    public ResponseEntity<String> cancel(@PathVariable("bookingId") int id) {
        try {
            Booking b = bookingService.findById(id);
            b.setBookingStatus(0);
            bookingService.save(b);
            return ResponseEntity.ok("OK.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed.");
        }
    }
}
