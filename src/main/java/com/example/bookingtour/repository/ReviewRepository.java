package com.example.bookingtour.repository;


import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Review;
import org.springframework.stereotype.Repository;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review,Integer> {
    @Query("SELECT r.review_id, r.tour_id, r.customer_id, r.rating, r.review_comment, r.review_date, r.status, " +
            "t.tour_name, t.tour_type, t.tour_id, " +
            "c.name, c.email, c.phone_number, c.customer_id " +
            "FROM Review r " +
            "LEFT JOIN Tour t ON r.tour_id = t.tour_id " +
            "LEFT JOIN Customer c ON r.customer_id = c.customer_id " +
            "WHERE r.review_id = :id"
    )
    List<Object[]> findReviewById(@Param("id") int id);

    @Query("SELECT r.review_id, r.tour_id, r.customer_id, r.rating, r.review_comment, r.review_date, r.status, " +
            "t.tour_name, t.tour_type, t.tour_id, " +
            "c.name, c.email, c.phone_number, c.customer_id " +
            "FROM Review r " +
            "LEFT JOIN Tour t ON r.tour_id = t.tour_id " +
            "LEFT JOIN Customer c ON r.customer_id = c.customer_id ")
    List<Object[]> getAllReviews();

    @Transactional
    @Query("SELECT r.review_id, r.tour_id, r.customer_id, r.rating, r.review_comment, r.review_date, r.status, " +
            "t.tour_name, t.tour_type, t.tour_id, " +
            "c.name, c.email, c.phone_number, c.customer_id " +
            "FROM Review r " +
            "LEFT JOIN Tour t ON r.tour_id = t.tour_id " +
            "LEFT JOIN Customer c ON r.customer_id = c.customer_id " +
            "WHERE " +
            "LOWER(t.tour_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.tour_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "

    )
    List<Object[]> searchReviews(@Param("searchTerm") String searchTerm);
}


