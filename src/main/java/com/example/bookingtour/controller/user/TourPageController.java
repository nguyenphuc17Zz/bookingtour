package com.example.bookingtour.controller.user;

import com.example.bookingtour.dto.TourFilters;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TourPageController {
    @Autowired
    private TourService tourService;
    @GetMapping("/tour")
    public String tourPage(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "tour_type", required = false, defaultValue = "") String tourType,
                           @RequestParam(value = "rating", required = false, defaultValue = "0") int rating,
                           @RequestParam(value = "available_seats", required = false, defaultValue = "0") int availableSeats,
                           @RequestParam(value = "start_date", required = false, defaultValue = "2024-01-01") String startDate,
                           @RequestParam(value = "end_date", required = false, defaultValue = "2024-01-01") String endDate,
                           @RequestParam(value = "search", required = false, defaultValue = "") String search
    ) {
        List<Tour> tours = tourService.getAllToursActive();
        if(!search.isEmpty()){
            tours = tours.stream()
                    .filter(tour -> tour.getTour_name().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (!tourType.isEmpty()) {
            tours = tours.stream().filter(t -> t.getTour_type().equalsIgnoreCase(tourType)).collect(Collectors.toList());
        }

        if (rating > 0) {
            tours = tours.stream().filter(t -> t.getRating() == rating).collect(Collectors.toList());
        }

        if (availableSeats > 0) {
            tours = tours.stream().filter(t -> t.getAvailable_seats()  >= availableSeats).collect(Collectors.toList());
        }

        if (!startDate.equals("2024-01-01")) {
            LocalDate start = LocalDate.parse(startDate);
            tours = tours.stream().filter(t -> !t.getStart_date().isBefore(start)).collect(Collectors.toList());
        }

        if (!endDate.equals("2024-01-01")) {
            LocalDate end = LocalDate.parse(endDate);
            tours = tours.stream().filter(t -> !t.getEnd_date().isAfter(end)).collect(Collectors.toList());
        }


            tours = tours.stream().map(tour -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            tour.setStartDateFormatted(tour.getStart_date() != null ? tour.getStart_date().format(formatter) : "N/A");
            tour.setEndDateFormatted(tour.getEnd_date() != null ? tour.getEnd_date().format(formatter) : "N/A");

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            String priceFormatted = tour.getPrice() != 0 ? decimalFormat.format(tour.getPrice()) : "N/A";
            tour.setFormatCurrencyVnd(priceFormatted + " VND");
            tour.setTourRating(roundRating(tour.getRating()));
            return tour;
        }).collect(Collectors.toList());

        int resultsPerPage = 9 ;
        int totalPages = (int) Math.ceil((double) tours.size() / resultsPerPage);
        int start = (page-1)*resultsPerPage;
        int end = Math.min(start + resultsPerPage, tours.size());

        List<Tour> paginatedTours = tours.subList(start, end);
        TourFilters tourFilters = new TourFilters(tourType, rating, availableSeats, startDate, endDate);

        model.addAttribute("tours",paginatedTours);
        model.addAttribute("page",page);
        model.addAttribute("total",totalPages);
        model.addAttribute("tourFilters",tourFilters);
        model.addAttribute("search",search);
        return "user/tour/tour";
    }
    public int roundRating(double x){
        return (int) Math.round(x);
    }
}
