package com.example.bookingtour.controller;

import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.entity.Customer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/login")
    public String showLoginPage(Model model) {
        model.addAttribute("admin",new Admin());
        return "admin/authentication/login";
    }

    @PostMapping("/admin/login/send")
    public String processLogin(@ModelAttribute("admin") Admin admin, RedirectAttributes ra, HttpServletResponse response) {
        Admin admin1 = adminService.findAdminByEmail(admin.getEmail());
        if (admin1 != null && admin1.getPassword().equals(admin.getPassword())) {
            if(!admin1.isStatus()){
                ra.addFlashAttribute("message", "Tài khoản đã bị khóa!");
                return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
            }
            createCookie(response, String.valueOf(admin1.getAdmin_id()));
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
    public  String processForgotPass(@ModelAttribute("forgotpass") ForgotPassDto forgotpass, Model model,RedirectAttributes ra){
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
    // GO TO SETTING PROFILE
    @GetMapping("/admin/setting")
    public String showSettingPage(HttpServletRequest request,Model model){
        Cookie cookie = getCookie(request,"adminId");
        Admin admin = adminService.findById(Integer.parseInt(cookie.getValue()));
        model.addAttribute("admin",admin);
        return "admin/authentication/setting";
    }
    @PostMapping("/admin/setting/send")
    public String processChangeProfileAdmin(@ModelAttribute("admin") Admin admin , RedirectAttributes ra, HttpServletRequest request) {
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
    private Comparable<?> getPropertyValue(Admin admin, String propertyName) {
        if ("name".equals(propertyName)) {
            return admin.getAdmin_name(); // String
        } else if ("email".equals(propertyName)) {
            return admin.getEmail(); // String
        } else if ("status".equals(propertyName)) {
            return admin.isStatus() ? 1 : 0;
        }else if("role".equals(propertyName)){
            return admin.getRole();
        }
        return null;
    }
    @GetMapping("/admin/qtv")
    public String showPageIndexAdmin(
            Model model,
            @RequestParam(value="searchTerm",required=false) String searchTerm,
            @RequestParam(value = "resultsPerPage" , required = false, defaultValue = "10") int resultsPerPage,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sort" , defaultValue = "name" , required = false) String sort,
            @RequestParam(value="order", defaultValue = "asc", required = false) String order
    ){
        List<Admin> admins;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            if(searchTerm.equals("Quản lý tour")){
                searchTerm="tour_manager";
            }else if(searchTerm.equals("Quản lý đặt lịch")){
                searchTerm="booking_manager";
            }
            admins = adminService.searchAdmins(searchTerm);
        } else {
            admins = adminService.getAllAdmins();
        }
        if(admins.isEmpty()){
            model.addAttribute("message","Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            admins.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            admins.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalCustomers = admins.size();
        int totalPages = (int) Math.ceil((double) totalCustomers / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalCustomers);

        List<Admin> paginatedAdmins = admins.subList(start, end);

        model.addAttribute("admins", paginatedAdmins);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort",sort);
        model.addAttribute("order", order);
        return "admin/qtv/index";

    }
    @PostMapping("admin/qtv/restore/{id}")
    @ResponseBody
    public ResponseEntity<String> restoreAdmin(@PathVariable("id") int id) {
        try {
            adminService.restoreAdmin(id);
            return ResponseEntity.ok("Khôi phục Admin thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục Admin thất bại.");
        }
    }
    @PostMapping("admin/qtv/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") int id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Xóa admin thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa admin thất bại.");
        }
    }
    @GetMapping("admin/qtv/edit/{id}")
    public String showPageEditAdmin(@PathVariable("id") int id,Model model){
        Admin admin = this.adminService.findById(id);

        model.addAttribute("admin",admin);
        return "admin/qtv/edit";
    }
    @PostMapping("admin/qtv/update/{id}/send")
    public String executeEditAdmin(@PathVariable("id") int id , @ModelAttribute Admin admin, RedirectAttributes ra){

        Admin a = this.adminService.findById(id);
        a.setAdmin_name(admin.getAdmin_name());
        a.setEmail(admin.getEmail());
        a.setRole(admin.getRole());
        a.setStatus(admin.isStatus());
        // Lưu khách hàng đã cập nhật
        adminService.save(a);
        ra.addFlashAttribute("message","Cập nhật thành công");
        return String.format("redirect:/admin/qtv/edit/%d", id);
    }
    @GetMapping("admin/qtv/add")
    public String showPageAddAdmin(Model model){
        model.addAttribute("admin",new Admin());
        return "admin/qtv/add";
    }
    @PostMapping("admin/qtv/add/send")
    public String executeAddAdmin(@ModelAttribute Admin admin, RedirectAttributes ra) {
        admin.setPassword("1");
        adminService.save(admin);
        ra.addFlashAttribute("message", "Thêm mới admin thành công");
        return "redirect:/admin/qtv/add";
    }
}
