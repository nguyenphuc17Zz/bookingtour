package com.example.bookingtour.controller;

import com.example.bookingtour.dto.AddReviewTourDetail;
import com.example.bookingtour.dto.UserProfileDto;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Review;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.service.ReviewService;
import com.example.bookingtour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TourDetailController {
    @Autowired
    private TourService tourService;
    @Autowired
    private ReviewService reviewService;
    @GetMapping("tour_detail/{id}")
    public String showPageEditTour(@PathVariable("id") int id, Model model) {
        Tour tour = tourService.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");



        tour.setStartDateFormatted(tour.getStart_date().format((formatter)));
        tour.setEndDateFormatted(tour.getEnd_date().format((formatter)));
        tour.setFormatCurrencyVnd(decimalFormat.format(tour.getPrice())+ " VND");
        tour.setTourRating(roundRating(tour.getRating()));
        model.addAttribute("tour", tour);
        return "user/tour_detail/tour_detail";
    }

    public int roundRating(double x) {
        return (int) Math.round(x);
    }

    @PostMapping("review/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addReview(@RequestBody AddReviewTourDetail review) {
        Map<String, Object> response = new HashMap<>();
        Review r = new Review();
        r.setTourId(review.getTour_id());
        r.setCustomerId(review.getCustomer_id());
        r.setRating(review.getRating());
        r.setReviewComment(review.getReview_comment());
        r.setReviewDate(review.getReview_date());
        r.setStatus(review.getStatus());


        reviewService.save(r);
        response.put("success", true);
        response.put("message", "Đăng review thành công");
        return ResponseEntity.ok(response);
        }


}
