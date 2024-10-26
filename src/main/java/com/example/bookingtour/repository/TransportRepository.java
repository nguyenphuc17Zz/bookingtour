package com.example.bookingtour.repository;


import com.example.bookingtour.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport,Integer> {
//    @Query(value = "SELECT * FROM customers WHERE email = :email", nativeQuery = true)
//    Customer findCustomerByEmail(@Param("email") String email);
//
//    @Query(value = "SELECT * FROM customers WHERE phone_number = :phoneNumber", nativeQuery = true)
//    Customer findCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber);
//
//    @Transactional
//    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "OR LOWER(c.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    public List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);
}
