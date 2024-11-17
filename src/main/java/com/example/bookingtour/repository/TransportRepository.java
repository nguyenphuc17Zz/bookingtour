package com.example.bookingtour.repository;


import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport,Integer> {
    @Modifying
    @Transactional
    @Query("SELECT a FROM Transport a WHERE LOWER(a.transportation_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.tag) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ")
    public List<Transport> searchTransports(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM transportation WHERE transportation_id = :id", nativeQuery = true)
    Transport findTransportById(@Param("id") int id);

    @Transactional
    @Query("SELECT t FROM Transport t WHERE t.status = 1")
    List<Transport> getAllTransportsActive();
}
