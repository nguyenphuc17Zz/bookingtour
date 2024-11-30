package com.example.bookingtour.vnpay;


import com.example.bookingtour.dto.BookingPageDto;
import com.example.bookingtour.dto.VNPayDto;
import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.service.BookingService;
import com.example.bookingtour.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.example.bookingtour.vnpay.VNPayConfig.*;

@Controller
public class VNPayController {

    @Autowired
    private VNPayService vnpayService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private NotificationService notificationService;
    @PostMapping("create_payment")
    public ResponseEntity<?> submitOrder(HttpServletRequest request, @RequestBody BookingPageDto b) {
        Long amount = Math.round(b.getTotal_price());
        try {
            String paymentUrl = vnpayService.createOrder(
                    request,
                    amount,
                    "Đặt tour " + b.getTour_id(),
                    b.getCustomer_id(),
                    b.getTour_id(),
                    b.getNum_guests(),
                    b.getTotal_price(),
                    b.getPayment_status(),
                    b.getSpecial_request()
            );

            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi tạo thanh toán.");
        }
    }


    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/return_vnpay/{customerId}/{tourId}/{numGuests}/{totalPrice}/{paymentStatus}/{specialRequest}")
    public String paymentCompleted(HttpServletRequest request
    , @PathVariable("customerId") int customerId, @PathVariable("tourId") int tourId, @PathVariable("numGuests") int numGuests,
                                    @PathVariable("totalPrice") double totalPrice, @PathVariable("paymentStatus") int paymentStatus,
                                    @PathVariable("specialRequest") String specialRequest
    ) {
        Booking b = new Booking();
        b.setCustomerId(customerId);
        b.setTourId(tourId);
        b.setBookingDate(LocalDate.now());
        b.setNumGuests(numGuests);
        b.setTotalPrice(totalPrice);
        b.setPaymentStatus(paymentStatus);
        b.setBookingStatus(2);
        bookingService.save(b);

        Notification notification = new Notification();
        notification.setStatus("unread");
        notification.setCustomer_id(customerId);
        notification.setMessage("Đặt tour "+tourId +" và thanh toán VNPAY thành công");
        notification.setSend_date(LocalDate.now());
        notificationService.saveNotification(notification);

        return String.format("redirect:/history?id=%d", customerId);
    }
}
