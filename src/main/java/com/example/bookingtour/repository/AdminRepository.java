package com.example.bookingtour.repository;

import com.example.bookingtour.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findAdminByEmail(String email);
    // SQL
    /*
    @Query(value = "SELECT * FROM admins WHERE email = :email", nativeQuery = true)
    Admin findAdminByEmailNative(@Param("email") String email);
     */
}
