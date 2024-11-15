package com.example.bookingtour.service;


import com.example.bookingtour.entity.Review;
import com.example.bookingtour.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.repository.AdminRepository;
import com.example.bookingtour.repository.CustomerRepository;
import com.example.bookingtour.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Object[]> getAllReviews(){
        return reviewRepository.getAllReviews();
    }

    public List<Object[]> searchReviews(String searchTerm){
        return reviewRepository.searchReviews(searchTerm);
    }

    public  List<Object[]> findById(int id){
        return reviewRepository.findReviewById(id);
    }

    public void deleteReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus(0);
        reviewRepository.save(review);
    }

    public void restoreReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus(1);
        reviewRepository.save(review);
    }
    public void save(Review r){
        reviewRepository.save(r);
    }
}


