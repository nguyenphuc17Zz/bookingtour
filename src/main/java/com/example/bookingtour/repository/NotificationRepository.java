package com.example.bookingtour.repository;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT n.notification_id, n.message, n.send_date, n.status, c.name, c.customer_id " +
            "FROM Notification n INNER JOIN Customer c ON n.customer_id = c.customer_id")
    List<Object[]> getAllNotifications();

    @Transactional
    @Query("SELECT n.notification_id, n.message, n.send_date, n.status, c.name, c.customer_id FROM Notification n INNER JOIN Customer c ON n.customer_id = c.customer_id " +
            "WHERE LOWER(n.message) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Object[]> searchNotifications(@Param("searchTerm") String searchTerm);

}
