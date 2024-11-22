package com.example.bookingtour.repository;

import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository  extends JpaRepository<Booking,Integer> {

//    @Modifying
//    @Transactional
    @Query(value = "SELECT * FROM bookings WHERE booking_id = :id", nativeQuery = true)
    Booking findBookingById(@Param("id") int id);

    @Query(value = "SELECT b.booking_id, b.tour_id, b.transportation_id, b.booking_date, b.num_guests, b.total_price, b.payment_status, b.special_requests, b.booking_status, " +
            "t.transportation_type, t.transportation_id, " +
            "c.name, c.customer_id, c.phone_number, " +
            "tour.tour_name, tour.tour_description " +
            "FROM bookings b " +
            "LEFT JOIN transportation t ON b.transportation_id = t.transportation_id " +
            "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
            "LEFT JOIN tour tour ON b.tour_id = tour.tour_id " +
            "WHERE b.booking_id = :id", nativeQuery = true)
    List<Object[]> findEditBookingById(@Param("id") int id);

    @Query("SELECT b.booking_id, b.tour_id, b.transportation_id, b.booking_date, b.num_guests, b.total_price, b.payment_status, b.special_requests, b.booking_status, " +
            "t.transportation_type, t.transportation_id, " +
            "c.name, c.customer_id, c.phone_number, " +
            "tour.tour_name, tour.tour_description " +
            "FROM Booking b " +
            "LEFT JOIN Transport t ON b.transportation_id = t.transportation_id " +
            "INNER JOIN Customer c ON b.customer_id = c.customer_id " +
            "INNER JOIN Tour tour ON b.tour_id = tour.tour_id")
    List<Object[]> getAllBookings();

    @Transactional
    @Query("SELECT b.booking_id, b.tour_id, b.transportation_id, b.booking_date, b.num_guests, b.total_price, b.payment_status, b.special_requests, b.booking_status, " +
            "t.transportation_type, t.transportation_id, " +
            "c.name, c.customer_id, c.phone_number, " +
            "tour.tour_name, tour.tour_description " +
            "FROM Booking b " +
            "INNER JOIN Transport t ON b.transportation_id = t.transportation_id " +
            "INNER JOIN Customer c ON b.customer_id = c.customer_id " +
            "INNER JOIN Tour tour ON b.tour_id = tour.tour_id " +
            "WHERE " +
            "LOWER(t.transportation_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(tour.tour_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
    )
    List<Object[]> searchBookings(@Param("searchTerm") String searchTerm);


    @Query("SELECT b.booking_id, b.tour_id, b.transportation_id, b.booking_date, b.num_guests, b.total_price, b.payment_status, b.special_requests, b.booking_status, " +
            "t.transportation_type, t.transportation_id, " +
            "c.name, c.customer_id, c.phone_number, " +
            "tour.tour_name, tour.tour_description " +
            "FROM Booking b " +
            "LEFT JOIN Transport t ON b.transportation_id = t.transportation_id " +
            "INNER JOIN Customer c ON b.customer_id = c.customer_id " +
            "INNER JOIN Tour tour ON b.tour_id = tour.tour_id WHERE c.customer_id=:id ORDER BY b.booking_id DESC")
    List<Object[]> getAllBookingsByUserId(@Param("id") int id);
    @Query(value = "SELECT COUNT(*) FROM bookings WHERE booking_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)", nativeQuery = true)
    int BookingMotThang();


    @Query("SELECT b.booking_id, b.tour_id, b.transportation_id, b.booking_date, b.num_guests, b.total_price, b.payment_status, b.special_requests, b.booking_status, " +
            "t.transportation_type, t.transportation_id, " +
            "c.name, c.customer_id, c.phone_number, " +
            "tour.tour_name, tour.tour_description, tour.tour_type " +
            "FROM Booking b " +
            "LEFT JOIN Transport t ON b.transportation_id = t.transportation_id " +
            "INNER JOIN Customer c ON b.customer_id = c.customer_id " +
            "INNER JOIN Tour tour ON b.tour_id = tour.tour_id")
    List<Object[]> getAllBookingsWithTourType();

    @Query("SELECT t.tour_name,b.booking_date, COUNT(b.tour_id) AS number_of_bookings " +
            "FROM Booking b " +
            "INNER JOIN Tour t ON b.tour_id = t.tour_id " +
            "WHERE b.booking_status = 2 " +
            "GROUP BY t.tour_id " +
            "ORDER BY number_of_bookings DESC")
    List<Object[]> getTop5Tour();



}
