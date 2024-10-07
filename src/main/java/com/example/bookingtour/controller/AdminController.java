package com.example.bookingtour.controller;

import com.example.bookingtour.dto.ForgotPassDto;
import org.springframework.ui.Model;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/login")
    public String showLoginPage(Model model) {
        model.addAttribute("admin",new Admin());
        return "admin/login";
    }

    @PostMapping("/admin/login/send")
    public String processLogin(@ModelAttribute("admin") Admin admin, Model model) {
        Admin admin1 = adminService.findAdminByEmail(admin.getEmail());
        if (admin1 != null && admin1.getPassword().equals(admin.getPassword())) {
            model.addAttribute("message", "Đăng nhập thành công!");
            return "admin/login";
        } else {
            model.addAttribute("message", "Thông tin đăng nhập không chính xác!");
            return "/admin/login";
        }
    }

    @GetMapping("/admin/forgotpass")
    public String showForgotPassPage(Model model) {
        model.addAttribute("forgotpass", new ForgotPassDto());
        return "admin/forgotpass";
    }

    @PostMapping("/admin/forgotpass/send")
    public  String processForgotPass(@ModelAttribute("forgotpass") ForgotPassDto forgotpass, Model model){
        Admin admin1 = this.adminService.findAdminByEmail(forgotpass.getEmail());
        System.out.println(forgotpass.getNewPassword());
        System.out.println(forgotpass.getConfirmPassword());

        if (admin1 != null) {
            if (forgotpass.getNewPassword().equals(forgotpass.getConfirmPassword())) {
                admin1.setPassword(forgotpass.getNewPassword());
                adminService.updateAdmin(admin1);
                model.addAttribute("message", "Mật khẩu đã được cập nhật thành công!");
                return "admin/login";
            } else {
                model.addAttribute("message", "Mật khẩu không khớp!");
                return "admin/forgotpass";
            }
        } else {
            model.addAttribute("message", "Không tìm thấy email này!");
            return "admin/forgotpass";
        }
    }
    @GetMapping("/admin/index")
    public String showIndexPage(Model model) {
        return "admin/index";
    }

}
