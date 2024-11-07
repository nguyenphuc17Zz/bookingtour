package com.example.bookingtour.controller;

import com.example.bookingtour.dto.CustomerRegisterDto;
import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.service.AdminService;
import com.example.bookingtour.service.CustomerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AuthenticationController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/login")
    public String showLoginPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/authentication/login";
    }

    @PostMapping("/admin/login/send")
    public String processLogin(@ModelAttribute("admin") Admin admin, RedirectAttributes ra, HttpServletResponse response) {
        Admin admin1 = adminService.findAdminByEmail(admin.getEmail());
        if (admin1 != null && admin1.getPassword().equals(admin.getPassword())) {
            if (!admin1.isStatus()) {
                ra.addFlashAttribute("message", "Tài khoản đã bị khóa!");
                return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
            }
            createCookie(response,"adminId", String.valueOf(admin1.getAdmin_id()));
            createCookie(response,"role",admin1.getRole());
            return "redirect:/admin/index";
        } else {
            ra.addFlashAttribute("message", "Thông tin đăng nhập không chính xác!");
            return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
        }
    }


    @GetMapping("/admin/forgotpass")
    public String showForgotPassPage(Model model) {
        model.addAttribute("forgotpass", new ForgotPassDto());
        return "admin/authentication/forgotpass";
    }

    // XU LI QUEN MAT KHAU
    @PostMapping("/admin/forgotpass/send")
    public String processForgotPass(@ModelAttribute("forgotpass") ForgotPassDto forgotpass, Model model, RedirectAttributes ra) {
        Admin admin1 = this.adminService.findAdminByEmail(forgotpass.getEmail());
        System.out.println(forgotpass.getNewPassword());
        System.out.println(forgotpass.getConfirmPassword());

        if (admin1 != null) {
            if (forgotpass.getNewPassword().equals(forgotpass.getConfirmPassword())) {
                admin1.setPassword(forgotpass.getNewPassword());
                adminService.updateAdmin(admin1);
                model.addAttribute("message", "Mật khẩu đã được cập nhật thành công!");
                return "admin/authentication/login";
            } else {
                ra.addFlashAttribute("message", "Mật khẩu không khớp!");
                return "redirect:/admin/forgotpass"; // Chuyển hướng để giữ lại form
            }
        } else {
            ra.addFlashAttribute("message", "Không tìm thấy email này!");
            return "redirect:/admin/forgotpass"; // Chuyển hướng để giữ lại form
        }
    }

    // LAY COOKIE
    private Cookie getCookie(HttpServletRequest request, String name) {
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
    public String showIndexPage(Model model, HttpServletRequest request) {
        Cookie cookie = getCookie(request, "adminId");
        if (cookie != null) {
            // model.addAttribute("message", "Cookie exists: " + cookie.getValue());
            return "admin/index";
        } else {
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/admin")
    public String goToIndex() {
        return "redirect:/admin/index";
    }

    // TAO COOKIE
    public void createCookie(HttpServletResponse response,String name ,String id) {
        Cookie cookie = new Cookie(name, id);

        cookie.setMaxAge(60 * 60 * 24);

        cookie.setPath("/");

        response.addCookie(cookie);

    }

    // DELETE COOKIE
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // XU LI LOGOUT
    @GetMapping("/admin/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response, String name) {
        deleteCookie(request, response, "adminId");
        return "redirect:/admin/login";
    }

    // GO TO SETTING PROFILE
    @GetMapping("/admin/setting")
    public String showSettingPage(HttpServletRequest request, Model model) {
        Cookie cookie = getCookie(request, "adminId");
        Admin admin = adminService.findById(Integer.parseInt(cookie.getValue()));
        model.addAttribute("admin", admin);
        return "admin/authentication/setting";
    }

    @PostMapping("/admin/setting/send")
    public String processChangeProfileAdmin(@ModelAttribute("admin") Admin admin, RedirectAttributes ra, HttpServletRequest request) {
        Cookie cookie = getCookie(request, "adminId");
        Admin currentAdmin = adminService.findById(Integer.parseInt(cookie.getValue()));

        // Kiểm tra xem có thay đổi nào không
        if (currentAdmin.getAdmin_name().equals(admin.getAdmin_name()) &&
                currentAdmin.getEmail().equals(admin.getEmail()) &&
                currentAdmin.getPassword().equals(admin.getPassword())) {
            ra.addFlashAttribute("message", "Không có gì thay đổi");
            return "redirect:/admin/setting";
        } else {
            // Cập nhật thông tin admin
            System.out.println(admin.getAdmin_id());
            System.out.println(admin.getAdmin_name());
            int updatedCount = this.adminService.updateAdminSetting(currentAdmin.getAdmin_id(), admin.getAdmin_name(), admin.getEmail(), admin.getPassword(), admin.getRole());

            if (updatedCount > 0) {
                ra.addFlashAttribute("message", "Chỉnh sửa thông tin thành công");
            } else {
                ra.addFlashAttribute("message", "Không thể cập nhật thông tin admin");
            }

            return "redirect:/admin/setting";
        }
    }


    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("customer", new CustomerRegisterDto());
        return "user/authentication/register";
    }

    @PostMapping("/customer/register/send")
    public String processRegister(@ModelAttribute("customer") CustomerRegisterDto c, Model model) {
        String email = c.getEmail();
        String phoneNumber = c.getPhoneNumber();
        String name = c.getName();
        String address = c.getAddress();
        String password = c.getPassword();
        String confirmPass = c.getConfirmPass();
        LocalDateTime now = LocalDateTime.now();
        Customer cus_a = customerService.findByEmail(email);
        if (cus_a != null) {
            model.addAttribute("message", "Email đã tồn tại");
            return "user/authentication/register";
        }
        Customer cus_b = customerService.findByPhoneNumber(phoneNumber);
        if (cus_b != null) {
            model.addAttribute("message", "Số điện thoại đã tồn tại");
            return "user/authentication/register";
        }
        if (!password.equals(confirmPass)) {
            model.addAttribute("message", "Nhập lại mật khẩu chưa chính xác");
            return "user/authentication/register";
        }
        Customer cus = new Customer(name, email, phoneNumber, password, address, now, true);
        this.customerService.add_update(cus);
        model.addAttribute("message", "Đăng kí thành công");
        return "redirect:/customer/login";
    }

    @GetMapping("/customer/login")
    public String showLoginPageCus(Model model) {
        model.addAttribute("customer", new Customer());
        return "user/authentication/login";
    }

    @PostMapping("/customer/login/send")
    public String processLogin(@ModelAttribute("customer") Customer c, Model model, RedirectAttributes ra) {
        Customer cus = customerService.findByEmail(c.getEmail());
        if (cus == null) {
            model.addAttribute("message", "Không tìm thấy email");
            return "user/authentication/login";
        } else {
            if (!cus.getPassword().equals(c.getPassword())) {
                model.addAttribute("message", "Sai mật khẩu");
                return "user/authentication/login";
            } else {
                if (!cus.isStatus()) {
                    model.addAttribute("message", "Tài khoản đã bị khóa");
                    return "user/authentication/login";
                }
                model.addAttribute("message", "Đăng nhập thành công");
                return "user/authentication/login";
            }
        }
    }

    @GetMapping("/customer/forgotpass")
    public String showForgotPassPageCus(Model model) {
        model.addAttribute("customer", new ForgotPassDto());
        return "user/authentication/forgotpass";
    }

    @PostMapping("/customer/forgotpass/send")
    public String processForgotPass(@ModelAttribute("customer") ForgotPassDto f, Model model) {
        Customer cus = customerService.findByEmail(f.getEmail());
        if (cus == null) {
            model.addAttribute("message", "Không tìm thấy email");
            return "user/authentication/forgotpass";
        } else {
            if (!f.getNewPassword().equals(f.getConfirmPassword())) {
                model.addAttribute("message", "Mật khẩu nhập lại không đúng");
                return "user/authentication/forgotpass";
            } else {
                cus.setPassword(f.getNewPassword());
                customerService.add_update(cus);
                model.addAttribute("message", "Đổi mật khẩu thành công");
                return "user/authentication/forgotpass";
            }
        }
    }
}
