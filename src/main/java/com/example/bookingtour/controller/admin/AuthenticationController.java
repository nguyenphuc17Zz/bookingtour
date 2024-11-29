package com.example.bookingtour.controller.admin;

import com.example.bookingtour.dto.CustomerRegisterDto;
import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.dto.LoginCustomerDto;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.security.JWTUtil;
import com.example.bookingtour.security.PasswordUtils;
import com.example.bookingtour.service.AdminService;
import com.example.bookingtour.service.CustomerService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthenticationController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordUtils passwordUtils;
    @GetMapping("/admin/login")
    public String showLoginPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/authentication/login";
    }

//    @PostMapping("/admin/login/send")
//    public String processLogin(@ModelAttribute("admin") Admin admin, RedirectAttributes ra, HttpServletResponse response) {
//        Admin admin1 = adminService.findAdminByEmail(admin.getEmail());
//        if (admin1 != null && admin1.getPassword().equals(admin.getPassword())) {
//            if (!admin1.isStatus()) {
//                ra.addFlashAttribute("message", "Tài khoản đã bị khóa!");
//                return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
//            }
//            createCookie(response,"adminId", String.valueOf(admin1.getAdmin_id()));
//            createCookie(response,"role",admin1.getRole());
//            return "redirect:/admin/index";
//        } else {
//            ra.addFlashAttribute("message", "Thông tin đăng nhập không chính xác!");
//            return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
//        }
//    }

    // XU LI LOGOUT
//    @GetMapping("/admin/logout")
//    public String processLogout(HttpServletRequest request, HttpServletResponse response, String name) {
//        deleteCookie(request, response, "adminId");
//        return "redirect:/admin/login";
//    }



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
                admin1.setPassword(passwordUtils.encodePassword(forgotpass.getNewPassword()));
                adminService.updateAdmin(admin1);
                model.addAttribute("message", "Mật khẩu đã được cập nhật thành công!");
                return "redirect:/admin/login";
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


            // Cập nhật thông tin admin
            System.out.println(admin.getAdmin_id());
            System.out.println(admin.getAdmin_name());
            int updatedCount = this.adminService.updateAdminSetting(currentAdmin.getAdmin_id(), admin.getAdmin_name(), admin.getEmail(), passwordUtils.encodePassword(admin.getPassword()), admin.getRole());

            if (updatedCount > 0) {
                ra.addFlashAttribute("message", "Chỉnh sửa thông tin thành công");
            } else {
                ra.addFlashAttribute("message", "Không thể cập nhật thông tin admin");
            }

            return "redirect:/admin/setting";

    }















    // AUTHENTICATION CUSTOMER
    @Autowired
    private CustomerService customerService;


    @GetMapping("/customer/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("customer", new CustomerRegisterDto());
        return "user/authentication/register";
    }

    @PostMapping("/register/send")
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

    @PostMapping("/login/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processLogin(@RequestBody LoginCustomerDto log) {
        Map<String, Object> response = new HashMap<>();
        Customer cus = customerService.findByEmail(log.getEmail());
        if (cus == null) {
            response.put("success", false);
            response.put("message", "Không tìm thấy email");
            return ResponseEntity.badRequest().body(response);
        } else {
            if (!cus.getPassword().equals(log.getPassword())) {
                response.put("success", false);
                response.put("message", "Sai mật khẩu");
                return ResponseEntity.badRequest().body(response);
            } else {


                if (!cus.isStatus()) {
                    response.put("success", false);
                    response.put("message", "Tài khoản đã bị khóa");
                    return ResponseEntity.badRequest().body(response);
                }
                try {
                    // Đăng nhập thành công, trả về JWT
                    String jwt = JWTUtil.createJWT(cus);  // Tạo JWT cho người dùng
                    response.put("success", true);
                    response.put("message", "Đăng nhập thành công");
                    response.put("jwt", jwt);  // Gửi JWT token về client
                    return ResponseEntity.ok(response);
                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.put("success", false);
                    response.put("message", "Có lỗi xảy ra: " + e.getMessage());  // Thêm thông báo lỗi chi tiết
                    return ResponseEntity.badRequest().body(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.put("success", false);
                    response.put("message", "Có lỗi xảy ra: " + e.getMessage());  // Thêm thông báo lỗi chi tiết
                    return ResponseEntity.badRequest().body(response);
                }


            }
        }
    }


    @GetMapping("/customer/forgotpass")
    public String showForgotPassPageCus(Model model) {
        model.addAttribute("customer", new ForgotPassDto());
        return "user/authentication/forgotpass";
    }

    @PostMapping("/forgotpass/send")
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
    @GetMapping("/api/user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Xác minh và giải mã token
                String userEmail = JWTUtil.parseJWT(token);
                Customer customer = customerService.findByEmail(userEmail);
                return ResponseEntity.ok(customer);
            } catch (JOSEException | ParseException e) {
                return ResponseEntity.status(401).body("Token không hợp lệ");
            }
        } else {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
    }
}
