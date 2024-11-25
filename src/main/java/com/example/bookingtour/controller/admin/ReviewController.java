package com.example.bookingtour.controller.admin;

import com.example.bookingtour.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

        private Comparable<?> getPropertyValue(Object[] n, String propertyName) {
        switch (propertyName) {
            case "tourName":
                return (Comparable<?>) n[7];
            case "tourType":
                return (Comparable<?>) n[8];
            case "cusName":
                return (Comparable<?>) n[10];
            case "phoneNumber":
                return (Comparable<?>) n[12];
            case "rating":
                return (Comparable<?>) n[3];
            case "reviewDate":
                return (Comparable<?>) n[5];
            case "status":
                return (Comparable<?>) n[6];
            default:
                return null;
        }
    }

    @GetMapping("/admin/review")
    public String showPageIndexAdmin(
            Model model,
            @RequestParam(value="searchTerm",required=false) String searchTerm,
            @RequestParam(value = "resultsPerPage" , required = false, defaultValue = "10") int resultsPerPage,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sort" , defaultValue = "name" , required = false) String sort,
            @RequestParam(value="order", defaultValue = "asc", required = false) String order
    ){
        List<Object[]> reviews;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            reviews = reviewService.searchReviews(searchTerm);
        } else {
            reviews = reviewService.getAllReviews();
        }

        if(reviews.isEmpty()){
            model.addAttribute("message","Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            reviews.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            reviews.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalReviews = reviews.size();
        int totalPages = (int) Math.ceil((double) totalReviews / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalReviews);

        List<Object[]> paginatedReviews = reviews.subList(start, end);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort",sort);
        model.addAttribute("order", order);
        model.addAttribute("reviews", paginatedReviews);
        return "admin/review/index";
    }

    @GetMapping("admin/review/delete/{id}")
    public String deleteReview(@PathVariable("id") int id){
        try {
            reviewService.deleteReview(id);
            return "redirect:/admin/review";

        } catch (Exception e) {
            return "redirect:/admin/review";
        }
    }

    @GetMapping("admin/review/restore/{id}")
    public String restoreReview(@PathVariable("id") int id){
        try {
            reviewService.restoreReview(id);
            return "redirect:/admin/review";

        } catch (Exception e) {
            return "redirect:/admin/review";
        }
    }

    @GetMapping("admin/review/edit/{id}")
    public String showPageEditTransport(@PathVariable("id") int id,Model model){
        List<Object[]> review = reviewService.findById(id);
        model.addAttribute("review",review);
        return "admin/review/edit";
    }


}

