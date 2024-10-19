package com.example.bookingtour.service;

import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    public Admin findAdminByEmail(String email){
        return this.adminRepository.findAdminByEmail(email);
    }
    public void updateAdmin(Admin admin){
        adminRepository.save(admin);
    }
    public Admin findById(int id){
         return adminRepository.findAdminById(id);
    }
    public int updateAdminSetting(int id, String name, String email, String pass, String role ){
        return adminRepository.updateAdminSetting(id,name,email,pass,role);
    }
    public List<Admin> searchAdmins(String searchTerm){
        return this.adminRepository.searchAdmins(searchTerm);
    }
    public void deleteAdmin(int adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Customer not found"));
        admin.setStatus(false);
        adminRepository.save(admin);
    }
    public void restoreAdmin(int adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Customer not found"));
        admin.setStatus(true);
        adminRepository.save(admin);
    }
    public void save(Admin admin) {
        adminRepository.save(admin);
    }
    public List<Admin> getAllAdmins(){
        return this.adminRepository.findAll();
    }
}
