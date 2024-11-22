package com.example.bookingtour.controller.admin;

import com.example.bookingtour.dto.ThongKeDoanhThuDto;
import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.service.BookingService;
import com.example.bookingtour.service.CustomerService;
import com.example.bookingtour.service.TourService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TourService    tourService;
    @Autowired
    private CustomerService customerService;


    // DI CHUYEN DEN TRANG INDEX
    @GetMapping("/admin/index")
    public String showIndexPage(Model model, HttpServletRequest request) {
        Cookie cookie = getCookie(request, "adminId");
        if (cookie != null) {
            int bookings = bookingService.BookingMotThang();
            model.addAttribute("bookings", bookings);
            int customers=customerService.countCustomer();
            model.addAttribute("customers",customers);
            int tours= tourService.countTour();
            model.addAttribute("tours",tours);
            List<Booking> bookingList = bookingService.getAllBooking();

            double doanhThu = 0;
            LocalDate today = LocalDate.now();
            LocalDate oneMonthAgo = today.minusDays(30);
            for (int i = 0; i < bookingList.size(); i++) {
                LocalDate bookingDate = bookingList.get(i).getBookingDate();
                if (bookingDate.isAfter(oneMonthAgo) || bookingDate.isEqual(oneMonthAgo)) {
                    if (bookingList.get(i).getBookingStatus() == 2) {
                        doanhThu += bookingList.get(i).getTotalPrice();
                    }
                }
            }
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            String return1 = decimalFormat.format(doanhThu)+" đ";
            model.addAttribute("return",return1);

            return "admin/index";
        } else {
            return "redirect:/admin/login";
        }
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    @PostMapping("admin/thongkedoanhthu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> thongKeDoanhThu(@RequestBody ThongKeDoanhThuDto request) {
        String tourType = request.getTourType();
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();

        List<Object[]> bookings = bookingService.getAllBookingsWithTourType();

        if (!"all".equalsIgnoreCase(tourType)) {
            bookings = bookings.stream()
                    .filter(b -> b[16].toString().equalsIgnoreCase(tourType))
                    .collect(Collectors.toList());
        }

        long totalDays = ChronoUnit.DAYS.between(fromDate, toDate);
        long interval = totalDays / 5;

        // Chỉ cần 5 khoảng thời gian
        List<LocalDate> intervals = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            intervals.add(fromDate.plusDays(i * interval));
        }
        intervals.set(5,toDate);

        // Tính doanh thu cho 5 khoảng
        List<Double> revenues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate start = intervals.get(i);
            LocalDate end = intervals.get(i + 1);

            double revenue = bookings.stream()
                    .filter(b -> {
                        LocalDate bookingDate = (LocalDate) b[3];  // b[3]: booking_date

                        return (bookingDate.isEqual(start) || bookingDate.isAfter(start)) &&
                                (bookingDate.isBefore(end) || bookingDate.isEqual(end))&&
                                ((int) b[8]) == 2;  // b[8]: booking_status
                    })
                    .mapToDouble(b -> (double) b[5])  // b[5]: total_price
                    .sum();

            revenues.add(revenue);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("intervals", intervals);
        response.put("revenues", revenues);

        return ResponseEntity.ok(response);
    }

    @PostMapping("admin/thongketour")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> thongKeTour(@RequestBody ThongKeDoanhThuDto request) {
        String tourType = request.getTourType();
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();
        List<Object[]> topTours  = bookingService.getTop5Tour();
        topTours = topTours.stream()
                .filter(t -> {
                    LocalDate bookingDate = (LocalDate) t[1];
                    return !bookingDate.isBefore(fromDate) && !bookingDate.isAfter(toDate);
                })
                .collect(Collectors.toList());
        List<Object[]> top5Tours = topTours.size() > 5 ? topTours.subList(0, 5) : topTours;
        Map<String, Object> response = new HashMap<>();
        response.put("data", top5Tours);
        return ResponseEntity.ok(response);
    }



}
