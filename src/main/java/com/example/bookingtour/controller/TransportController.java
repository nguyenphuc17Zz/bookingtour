package com.example.bookingtour.controller;

import com.example.bookingtour.dto.CustomerRegisterDto;
import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.dto.TransportDto;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class TransportController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/transportation")
    public String showRegisterPage(Model model) {
        model.addAttribute("transport",new TransportDto());
        return "admin/transportation/index";
    }

//
//
//    @PostMapping("/customer/register/send")
//    public String processRegister(@ModelAttribute("customer") CustomerRegisterDto c, Model model){
//        String email = c.getEmail();
//        String phoneNumber = c.getPhoneNumber();
//        String name = c.getName();
//        String address= c.getAddress();
//        String password = c.getPassword();
//        String confirmPass = c.getConfirmPass();
//        LocalDateTime now = LocalDateTime.now();
//        Customer cus_a = customerService.findByEmail(email);
//        if(cus_a!=null){
//            model.addAttribute("message","Email đã tồn tại");
//            return "user/authentication/register";
//        }
//        Customer cus_b= customerService.findByPhoneNumber(phoneNumber);
//        if(cus_b!=null){
//            model.addAttribute("message","Số điện thoại đã tồn tại");
//            return "user/authentication/register";
//        }
//        if(!password.equals(confirmPass)){
//            model.addAttribute("message","Nhập lại mật khẩu chưa chính xác");
//            return "user/authentication/register";
//        }
//        Customer cus = new Customer(name,email,phoneNumber,password,address,now,true);
//        this.customerService.add_update(cus);
//        model.addAttribute("message","Đăng kí thành công");
//        return "redirect:/customer/login";
//    }
//
//    @GetMapping("/customer/login")
//    public String showLoginPage(Model model){
//        model.addAttribute("customer",new Customer());
//        return "user/authentication/login";
//    }
//    @PostMapping("/customer/login/send")
//    public String processLogin(@ModelAttribute("customer") Customer c , Model model, RedirectAttributes ra){
//        Customer cus = customerService.findByEmail(c.getEmail());
//        if(cus==null){
//            model.addAttribute("message","Không tìm thấy email");
//            return "user/authentication/login";
//        }else{
//            if(!cus.getPassword().equals(c.getPassword())){
//                model.addAttribute("message","Sai mật khẩu");
//                return "user/authentication/login";
//            }else{
//                if(!cus.isStatus()){
//                    model.addAttribute("message","Tài khoản đã bị khóa");
//                    return "user/authentication/login";
//                }
//                model.addAttribute("message","Đăng nhập thành công");
//                return "user/authentication/login";
//            }
//        }
//    }
//
//    @GetMapping("/customer/forgotpass")
//    public String showForgotPassPage(Model model){
//        model.addAttribute("customer",new ForgotPassDto());
//        return "user/authentication/forgotpass";
//    }
//    @PostMapping("/customer/forgotpass/send")
//    public String processForgotPass(@ModelAttribute("customer") ForgotPassDto f , Model model){
//        Customer cus = customerService.findByEmail(f.getEmail());
//        if(cus==null){
//            model.addAttribute("message","Không tìm thấy email");
//            return "user/authentication/forgotpass";
//        }else{
//            if(!f.getNewPassword().equals(f.getConfirmPassword())){
//                model.addAttribute("message","Mật khẩu nhập lại không đúng");
//                return "user/authentication/forgotpass";
//            }else{
//                cus.setPassword(f.getNewPassword());
//                customerService.add_update(cus);
//                model.addAttribute("message","Đổi mật khẩu thành công");
//                return "user/authentication/forgotpass";
//            }
//        }
//    }
//
//
//
//
//    @GetMapping("/admin/customer")
//    public String showCustomerPage(Model model,
//                                   @RequestParam(value="searchTerm",required=false) String searchTerm,
//                                   @RequestParam(value = "resultsPerPage" , required = false, defaultValue = "10") int resultsPerPage,
//                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//                                   @RequestParam(value = "sort" , defaultValue = "name" , required = false) String sort,
//                                   @RequestParam(value="order", defaultValue = "asc", required = false) String order
//    ) {
//        List<Customer> customers;
//        if (searchTerm != null && !searchTerm.isEmpty()) {
//            customers = customerService.searchCustomers(searchTerm);
//        } else {
//            customers = customerService.getAllCustomers();
//        }
//        if(customers.isEmpty()){
//            model.addAttribute("message","Không tìm thấy kết quả phù hợp");
//        }
//        if ("asc".equals(order)) {
//            customers.sort((c1, c2) -> {
//                Comparable value1 = getPropertyValue(c1, sort);
//                Comparable value2 = getPropertyValue(c2, sort);
//                return value1 == null ? -1 : value1.compareTo(value2);
//            });
//        } else {
//            customers.sort((c1, c2) -> {
//                Comparable value1 = getPropertyValue(c1, sort);
//                Comparable value2 = getPropertyValue(c2, sort);
//                return value2 == null ? -1 : value2.compareTo(value1);
//            });
//        }
//
//
//        int totalCustomers = customers.size();
//        int totalPages = (int) Math.ceil((double) totalCustomers / resultsPerPage);
//        int start = (page - 1) * resultsPerPage;
//        int end = Math.min(start + resultsPerPage, totalCustomers);
//
//        List<Customer> paginatedCustomers = customers.subList(start, end);
//
//        model.addAttribute("customers", paginatedCustomers);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("resultsPerPage", resultsPerPage);
//        model.addAttribute("searchTerm", searchTerm);
//        model.addAttribute("sort",sort);
//        model.addAttribute("order", order);
//        return "admin/customer/cus_table";
//    }
//    private Comparable<?> getPropertyValue(Customer customer, String propertyName) {
//        if ("name".equals(propertyName)) {
//            return customer.getName(); // String
//        } else if ("email".equals(propertyName)) {
//            return customer.getEmail(); // String
//        } else if ("status".equals(propertyName)) {
//            return customer.isStatus() ? 1 : 0;
//        }
//        return null;
//    }
//    @PostMapping("admin/customer/delete/{customerId}")
//    @ResponseBody
//    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") int customerId) {
//        try {
//            customerService.deleteCustomer(customerId);
//            return ResponseEntity.ok("Xóa khách hàng thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa khách hàng thất bại.");
//        }
//    }
//    @PostMapping("admin/customer/restore/{customerId}")
//    @ResponseBody
//    public ResponseEntity<String> restoreCustomer(@PathVariable("customerId") int customerId) {
//        try {
//            customerService.restoreCustomer(customerId);
//            return ResponseEntity.ok("Khôi phục khách hàng thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục khách hàng thất bại.");
//        }
//    }
//    @GetMapping("admin/customer/edit/{id}")
//    public String showPageEditCustomer(@PathVariable("id") int id,Model model){
//        Customer c = this.customerService.findById(id);
//
//        model.addAttribute("customer",c);
//        return "admin/customer/cus_edit";
//    }
//    @PostMapping("admin/customer/update/{id}/send")
//    public String executeEditCustomer(@PathVariable("id") int id , @ModelAttribute Customer customer, RedirectAttributes ra){
//
//        Customer c = this.customerService.findById(id);
//        c.setName(customer.getName());
//        c.setEmail(customer.getEmail());
//        c.setPhone_number(customer.getPhone_number());
//        c.setAddress(customer.getAddress());
//        c.setStatus(customer.isStatus());
//
//        // Lưu khách hàng đã cập nhật
//        customerService.save(c);
//        ra.addFlashAttribute("message","Cập nhật thành công");
//        return String.format("redirect:/admin/customer/edit/%d", id);
//    }
//    @GetMapping("admin/customer/add")
//    public String showPageAddCustomer(Model model){
//        model.addAttribute("customer",new Customer());
//        return "admin/customer/cus_add";
//    }
//    @PostMapping("admin/customer/add/send")
//    public String executeAddCustomer(@ModelAttribute Customer customer, RedirectAttributes ra) {
//        customer.setPassword("1");
//        customerService.save(customer);
//        ra.addFlashAttribute("message", "Thêm mới khách hàng thành công");
//        return "redirect:/admin/customer/add";
//    }
}
