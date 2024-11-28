package com.example.bookingtour.repository;

import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findAdminByEmail(String email);
    // SQL
    /*
    @Query(value = "SELECT * FROM admins WHERE email = :email", nativeQuery = true)
    Admin findAdminByEmailNative(@Param("email") String email);
     */
    @Query(value = "SELECT * FROM admins WHERE admin_id = :id", nativeQuery = true)
    Admin findAdminById(@Param("id") int id);

    @Modifying
    @Transactional // Đảm bảo rằng phương thức này sẽ được thực hiện trong một giao dịch
    @Query(value = "UPDATE admins SET admin_name = :adminName, email = :email, password = :password, role = :role WHERE admin_id = :id", nativeQuery = true)
    int updateAdminSetting(@Param("id") int id,
                           @Param("adminName") String adminName,
                           @Param("email") String email,
                           @Param("password") String password,
                           @Param("role") String role);

    @Transactional
    @Query("SELECT a FROM Admin a WHERE LOWER(a.admin_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.role) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    public List<Admin> searchAdmins(@Param("searchTerm") String searchTerm);
    // SPRING SECURITY
    Optional<Admin> findByEmail(String email);

}
