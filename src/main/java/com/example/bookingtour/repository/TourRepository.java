package com.example.bookingtour.repository;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    @Transactional
    @Query("SELECT t FROM Tour t WHERE LOWER(t.tour_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    public List<Tour> searchTours(@Param("searchTerm") String searchTerm);
    @Transactional
    @Query("SELECT t FROM Tour t WHERE t.status = true and t.available_seats > 0 ORDER BY t.tour_id DESC")
    public List<Tour> getAllToursActive();
    @Query(value = "SELECT COUNT(*) FROM tour", nativeQuery = true)
    public int countTour();

}
