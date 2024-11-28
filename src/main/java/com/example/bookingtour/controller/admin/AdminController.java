package com.example.bookingtour.controller.admin;

import com.example.bookingtour.security.PasswordUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordUtils passwordUtils;
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
        Map<String, String> errors = validateAdmin(admin);
        if (!errors.isEmpty()) {
            errors.forEach((key, value) -> ra.addFlashAttribute("message", value));
            return String.format("redirect:/admin/qtv/edit/%d", id);
        }
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
        Map<String, String> errors = validateAdmin(admin);
        if (!errors.isEmpty()) {
            errors.forEach((key, value) -> ra.addFlashAttribute("message", value));
            return "redirect:/admin/qtv/add";
        }
        admin.setPassword(passwordUtils.encodePassword("1"));
        adminService.save(admin);
        ra.addFlashAttribute("message", "Thêm mới admin thành công");
        return "redirect:/admin/qtv/add";
    }
    private Map<String, String> validateAdmin(Admin admin) {
        Map<String, String> errors = new HashMap<>();

        if (admin.getAdmin_name() == null || admin.getAdmin_name().trim().isEmpty()) {
            errors.put("nameError", "Tên Admin không được để trống");
        }

        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty() || !admin.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("emailError", "Email không hợp lệ");
        }

        return errors;
    }
}
