package com.example.bookingtour.controller;

import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.repository.BookingRepository;
import com.example.bookingtour.repository.TransportRepository;
import com.example.bookingtour.service.BookingService;
import com.example.bookingtour.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private BookingService bookingService;
    private TransportService transportService;

    @Autowired
    public BookingController(TransportService transportService) {
        this.transportService = transportService;
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
        List<Transport> transports = transportService.getAllTransports();
        model.addAttribute("transports", transports);

        return "admin/booking/edit";
    }


    @PostMapping("admin/booking/update/{id}/send")
    public String executeEditTransport(
            @PathVariable("id") int id,
            @ModelAttribute Booking booking,
            RedirectAttributes ra,
            @RequestParam(value = "transportationId", required = false) Integer transportationId,
            @RequestParam("bookingStatus") int bookingStatus) {

        Booking b = this.bookingService.findById(id);

        if (transportationId != null) {
            b.setTransportationId(transportationId);
        }

        b.setBookingStatus(bookingStatus);
        bookingService.save(b);

        ra.addFlashAttribute("message", "Cập nhật thành công");
        return String.format("redirect:/admin/booking/edit/%d", id);
    }

}
