package com.example.bookingtour.controller;

import com.example.bookingtour.dto.ForgotPassDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String processLogin(@ModelAttribute("admin") Admin admin, Model model, HttpServletResponse response) {
        Admin admin1 = adminService.findAdminByEmail(admin.getEmail());
        if (admin1 != null && admin1.getPassword().equals(admin.getPassword())) {
            createCookie(response, String.valueOf(admin1.getAdmin_id()));
            return "redirect:/admin/index";
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
    // XU LI QUEN MAT KHAU
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
    // LAY COOKIE
    private Cookie getCookie(HttpServletRequest request , String name){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    // DI CHUYEN DEN TRANG INDEX
    @GetMapping("/admin/index")
    public String showIndexPage(Model model , HttpServletRequest request) {
        Cookie cookie = getCookie(request,"adminId");
        if (cookie != null) {
           // model.addAttribute("message", "Cookie exists: " + cookie.getValue());
            return "admin/index";
        } else {
            return "redirect:/admin/login";
        }
    }
    @GetMapping("/admin")
    public String goToIndex(){
        return "redirect:/admin/index";
    }
    // TAO COOKIE
    public void createCookie(HttpServletResponse response, String id) {
        Cookie cookie = new Cookie("adminId", id);

        cookie.setMaxAge(60* 60 * 24);

        cookie.setPath("/");

        response.addCookie(cookie);

    }
    // DELETE COOKIE
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    // XU LI LOGOUT
    @GetMapping("/admin/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response, String name){
        deleteCookie(request,response,"adminId");
        return "redirect:/admin/login";
    }
}
