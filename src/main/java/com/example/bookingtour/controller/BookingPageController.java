package com.example.bookingtour.controller;

import com.example.bookingtour.dto.AddReviewTourDetail;
import com.example.bookingtour.dto.BookingPageDto;
import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.entity.Review;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.service.BookingService;
import com.example.bookingtour.service.NotificationService;
import com.example.bookingtour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BookingPageController {
    @Autowired
    private TourService tourService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("booking/{id}")
    public String showFormBooking(@PathVariable("id") int id, Model model){
        Tour t = tourService.findById(id);
        model.addAttribute("tour",t);
        return "user/booking/booking";
    }
    @PostMapping("booking/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addBooking(@RequestBody BookingPageDto b) {
        Map<String, Object> response = new HashMap<>();
        Tour tour = tourService.findById(b.getTour_id());
        Booking booking = new Booking();
        booking.setCustomerId(b.getCustomer_id());
        booking.setTourId(b.getTour_id());
        booking.setBookingDate(b.getBooking_date());
        booking.setNumGuests(b.getNum_guests());
        booking.setTotalPrice(b.getTotal_price());
        booking.setPaymentStatus(b.getPayment_status());
        booking.setSpecialRequest(b.getSpecial_request());
        booking.setBookingStatus(b.getBooking_status());
        Notification noti = new Notification();
        noti.setCustomer_id(b.getCustomer_id());
        noti.setMessage("Đặt tour " + tour.getTour_name() +"thành công");
        noti.setStatus("unread");
        noti.setSend_date(b.getBooking_date());
        bookingService.save(booking);
        notificationService.saveNotification(noti);
        response.put("success", true);
        response.put("message", "Booking tour thành công");
        return ResponseEntity.ok(response);
    }
}
