package com.example.bookingtour.controller.user;

import com.example.bookingtour.dto.AddReviewTourDetail;
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
import java.util.List;
import java.util.Map;

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
        List<Object[]> reviews = reviewService.getAllReviewsByTourId(id);
      //  Object[] row = reviews.get(0);
//        System.out.println("Review ID: " + row[0]);       // Cột 1: review_id
//        System.out.println("Tour ID: " + row[1]);        // Cột 2: tour_id
//        System.out.println("Customer ID: " + row[2]);    // Cột 3: customer_id
//        System.out.println("Rating: " + row[3]);         // Cột 4: rating
//        System.out.println("Comment: " + row[4]);        // Cột 5: review_comment
//        System.out.println("Review Date: " + row[5]);    // Cột 6: review_date
//        System.out.println("Status: " + row[6]);         // Cột 7: status
//        System.out.println("Tour Name: " + row[7]);      // Cột 8: tour_name
//        System.out.println("Tour Type: " + row[8]);      // Cột 9: tour_type
//        System.out.println("Tour ID (duplicate): " + row[9]); // Cột 10: tour_id (có thể trùng cột 2)
//        System.out.println("Customer Name: " + row[10]); // Cột 11: name
//        System.out.println("Email: " + row[11]);         // Cột 12: email
//        System.out.println("Phone Number: " + row[12]);  // Cột 13: phone_number
//        System.out.println("Customer ID (duplicate): " + row[13]); // Cột 14: customer_id (có thể trùng cột 3)
//        System.out.println("---------------------------------------------------");
        int reviewLength=reviews.size();
        tour.setStartDateFormatted(tour.getStart_date().format((formatter)));
        tour.setEndDateFormatted(tour.getEnd_date().format((formatter)));
        tour.setFormatCurrencyVnd(decimalFormat.format(tour.getPrice())+ " VND");
        tour.setTourRating(roundRating(tour.getRating()));
        model.addAttribute("length",reviewLength);
        model.addAttribute("reviews",reviews);
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
