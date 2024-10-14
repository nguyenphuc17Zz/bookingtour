package com.example.bookingtour.controller;

import com.example.bookingtour.dto.CustomerRegisterDto;
import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/customer/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("customer",new CustomerRegisterDto());
        return "user/authentication/register";
    }
    @PostMapping("/customer/register/send")
    public String processRegister(@ModelAttribute("customer") CustomerRegisterDto c, Model model){
        String email = c.getEmail();
        String phoneNumber = c.getPhoneNumber();
        String name = c.getName();
        String address= c.getAddress();
        String password = c.getPassword();
        String confirmPass = c.getConfirmPass();
        LocalDateTime now = LocalDateTime.now();
        Customer cus_a = customerService.findByEmail(email);
        if(cus_a!=null){
            model.addAttribute("message","Email đã tồn tại");
            return "user/authentication/register";
        }
        Customer cus_b= customerService.findByPhoneNumber(phoneNumber);
        if(cus_b!=null){
            model.addAttribute("message","Số điện thoại đã tồn tại");
            return "user/authentication/register";
        }
        if(!password.equals(confirmPass)){
            model.addAttribute("message","Nhập lại mật khẩu chưa chính xác");
            return "user/authentication/register";
        }
        Customer cus = new Customer(name,email,phoneNumber,password,address,now,true);
        this.customerService.add_update(cus);
        model.addAttribute("message","Đăng kí thành công");
        return "user/authentication/register";
    }

    @GetMapping("/customer/login")
    public String showLoginPage(Model model){
        model.addAttribute("customer",new Customer());
        return "user/authentication/login";
    }
    @PostMapping("/customer/login/send")
    public String processLogin(@ModelAttribute("customer") Customer c , Model model, RedirectAttributes ra){
        Customer cus = customerService.findByEmail(c.getEmail());
        if(cus==null){
            model.addAttribute("message","Không tìm thấy email");
            return "user/authentication/login";
        }else{
            if(!cus.getPassword().equals(c.getPassword())){
                model.addAttribute("message","Sai mật khẩu");
                return "user/authentication/login";
            }else{
                if(!cus.isStatus()){
                    model.addAttribute("message","Tài khoản đã bị khóa");
                    return "user/authentication/login";
                }
                model.addAttribute("message","Đăng nhập thành công");
                return "user/authentication/login";
            }
        }
    }

    @GetMapping("/customer/forgotpass")
    public String showForgotPassPage(Model model){
        model.addAttribute("customer",new ForgotPassDto());
        return "user/authentication/forgotpass";
    }
    @PostMapping("/customer/forgotpass/send")
    public String processForgotPass(@ModelAttribute("customer") ForgotPassDto f , Model model){
        Customer cus = customerService.findByEmail(f.getEmail());
        if(cus==null){
            model.addAttribute("message","Không tìm thấy email");
            return "user/authentication/forgotpass";
        }else{
            if(!f.getNewPassword().equals(f.getConfirmPassword())){
                model.addAttribute("message","Mật khẩu nhập lại không đúng");
                return "user/authentication/forgotpass";
            }else{
                cus.setPassword(f.getNewPassword());
                customerService.add_update(cus);
                model.addAttribute("message","Đổi mật khẩu thành công");
                return "user/authentication/forgotpass";
            }
        }
    }




    @GetMapping("/admin/customer")
    public String showCustomerPage(Model model) {
        List<Customer> customers = this.customerService.getAllCustomers();
        model.addAttribute("customers",customers);
      //  int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        return "admin/customer/cus_table";
    }

    @GetMapping("/admin/customer/search")
    public String executeSearchCustomer(@RequestParam(value = "searchTerm",required = false) String searchTerm,Model model){
        System.out.println("Search Term: " + searchTerm);
        List<Customer> customers;
        if(searchTerm!=null && !searchTerm.isEmpty()){
            customers = customerService.searchCustomers(searchTerm);
        }else{
            customers=this.customerService.getAllCustomers();
        }
        model.addAttribute("customers",customers);
        if (customers.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy khách hàng nào.");
        }
        return "admin/customer/cus_table";
    }

}
