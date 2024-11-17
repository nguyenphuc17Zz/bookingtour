package com.example.bookingtour.controller;

import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.repository.BookingRepository;
import com.example.bookingtour.repository.TransportRepository;
import com.example.bookingtour.service.BookingService;
import com.example.bookingtour.service.NotificationService;
import com.example.bookingtour.service.TourService;
import com.example.bookingtour.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private BookingService bookingService;
    private TransportService transportService;
    private TourService tourService;
    private NotificationService notificationService;



    @Autowired
    public BookingController(TransportService transportService, TourService tourService,NotificationService notificationService ) {
        this.transportService = transportService;
        this.tourService = tourService;
        this.notificationService = notificationService;
    }


    private Comparable<?> getPropertyValue(Object[] n, String propertyName) {
        switch (propertyName) {
            case "tourName":
                return (Comparable<?>) n[14];
            case "name":
                return (Comparable<?>) n[11];
            case "transportationType":
                return (Comparable<?>) n[9];
            case "bookingDate":
                return (Comparable<?>) n[3];
            case "numGuests":
                return (Comparable<?>) n[4];
            case "totalPrice":
                return (Comparable<?>) n[5];
            case "phoneNumber":
                return (Comparable<?>) n[13];
            case "bookingStatus":
                return (Comparable<?>) n[8];
            default:
                return null;
        }
    }
    @GetMapping("/admin/booking")
    public String showPageIndexAdmin(
            Model model,
            @RequestParam(value="searchTerm",required=false) String searchTerm,
            @RequestParam(value = "resultsPerPage" , required = false, defaultValue = "10") int resultsPerPage,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sort" , defaultValue = "name" , required = false) String sort,
            @RequestParam(value="order", defaultValue = "asc", required = false) String order
    ){
        List<Object[]> bookings;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            bookings = bookingService.searchBookings(searchTerm);
        } else {
            bookings = bookingService.getAllBookings();
        }

        if(bookings.isEmpty()){
            model.addAttribute("message","Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            bookings.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            bookings.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalBookings = bookings.size();
        int totalPages = (int) Math.ceil((double) totalBookings / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalBookings);

        List<Object[]> paginatedBookings = bookings.subList(start, end);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort",sort);
        model.addAttribute("order", order);
        model.addAttribute("bookings", paginatedBookings);
        return "admin/booking/index";
    }




    @GetMapping("admin/booking/delete/{id}")
    public String deleteBooking(@PathVariable("id") int id){
        try {
            LocalDate now = LocalDate.now();
            Booking b = this.bookingService.findById(id);
            Notification noti = new Notification();
            noti.setCustomer_id(b.getCustomerId());
            noti.setMessage("Tour đã bị hủy");
            noti.setStatus("unread");
            noti.setSend_date(now);
            notificationService.saveNotification(noti);
            bookingService.deleteBooking(id);
            return "redirect:/admin/booking";

        } catch (Exception e) {
            return "redirect:/admin/booking";
        }
    }

    @GetMapping("admin/booking/restore/{id}")
    public String restoreBooking(@PathVariable("id") int id){
        try {
            bookingService.restoreBooking(id);
            return "redirect:/admin/booking";

        } catch (Exception e) {
            return "redirect:/admin/booking";
        }
    }

    @GetMapping("admin/booking/edit/{id}")
    public String showPageEditTransport(@PathVariable("id") int id,Model model){
        List<Object[]> booking = bookingService.findEditById(id);
        model.addAttribute("booking",booking);
        List<Transport> transports = transportService.getAllTransportsActive();
        model.addAttribute("transports", transports);

        return "admin/booking/edit";
    }


    @PostMapping("admin/booking/update/{id}/send")
    public String executeEditTransport(
            @PathVariable("id") int id,
            @ModelAttribute Booking booking,
            RedirectAttributes ra,
            @RequestParam(value = "transportationId", required = false) Integer transportationId,
            @RequestParam("bookingStatus") int bookingStatus,
            @RequestParam("numGuests") int numGuests,
            @RequestParam("tourId") int tourId,
            @RequestParam("customerId") int customerId
    )
    {
        Booking b = this.bookingService.findById(id);
        Tour t = this.tourService.findById(tourId);
        LocalDate now = LocalDate.now();

        if(numGuests > t.getAvailable_seats()){
            ra.addFlashAttribute("message", "Không đủ chỗ tour");
            return String.format("redirect:/admin/booking/edit/%d", id);
        }

        if (transportationId != null) {
            b.setTransportationId(transportationId);
        }

//        Kiểm tra trạng thái từ đang chờ thành trạng thái duyệt
//        case 1 trạng thái phải được duyệt, case 2 trạng thái trong db phải khác trạng thái submit
        if(bookingStatus == 2 && (b.getBookingStatus() != bookingStatus)){
            b.setBookingStatus(bookingStatus);
            Notification noti = new Notification();
            t.setAvailable_seats(t.getAvailable_seats()-numGuests);
            noti.setCustomer_id(customerId);
            noti.setMessage("Tour đã được duyệt thành công");
            noti.setStatus("unread");
            noti.setSend_date(now);
            notificationService.saveNotification(noti);
        }

        bookingService.save(b);
        tourService.saveTour(t);
        ra.addFlashAttribute("message", "Cập nhật thành công");
        return String.format("redirect:/admin/booking/edit/%d", id);
    }

}
