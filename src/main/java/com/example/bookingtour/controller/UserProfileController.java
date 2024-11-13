package com.example.bookingtour.controller;

import com.example.bookingtour.dto.LoginCustomerDto;
import com.example.bookingtour.dto.UserProfileDto;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.security.JWTUtil;
import com.example.bookingtour.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserProfileController {
    @Autowired
    private CustomerService customerService;


    @GetMapping("/user-profile")
    public String showUserProfilePage(@RequestParam("id") String id, Model model) {

        Customer customer = customerService.findById(Integer.parseInt(id));
        if (customer != null) {
            model.addAttribute("customer", customer);
            return "user/user-profile";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/user-profile/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changeInfo(@RequestBody UserProfileDto u) {
        Map<String, Object> response = new HashMap<>();
        Customer cus = customerService.findById(u.getId());
        if (cus == null) {
            response.put("success", false);
            response.put("message", "Không tìm thấy customer");
            return ResponseEntity.badRequest().body(response);
        } else {
            cus.setPassword(u.getPassword());
            cus.setPhone_number(u.getPhone());
            cus.setEmail(u.getEmail());
            cus.setAddress(u.getAddress());
            cus.setName(u.getName());
            customerService.save(cus);
            response.put("success", true);
            response.put("message", "Chỉnh sửa thành công");
            return ResponseEntity.ok(response);
            }

    }
}
