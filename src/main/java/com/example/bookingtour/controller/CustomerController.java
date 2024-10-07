package com.example.bookingtour.controller;

import com.example.bookingtour.dto.CustomerRegisterDto;
import com.example.bookingtour.dto.ForgotPassDto;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        return "user/register";
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
            return "user/register";
        }
        Customer cus_b= customerService.findByPhoneNumber(phoneNumber);
        if(cus_b!=null){
            model.addAttribute("message","Số điện thoại đã tồn tại");
            return "user/register";
        }
        if(!password.equals(confirmPass)){
            model.addAttribute("message","Nhập lại mật khẩu chưa chính xác");
            return "user/register";
        }
        Customer cus = new Customer(name,email,phoneNumber,password,address,now);
        this.customerService.add_update(cus);
        model.addAttribute("message","Đăng kí thành công");
        return "user/register";
    }

    @GetMapping("/customer/login")
    public String showLoginPage(Model model){
        model.addAttribute("customer",new Customer());
        return "user/login";
    }
    @PostMapping("/customer/login/send")
    public String processLogin(@ModelAttribute("customer") Customer c , Model model){
        Customer cus = customerService.findByEmail(c.getEmail());
        if(cus==null){
            model.addAttribute("message","Không tìm thấy email");
            return "user/login";
        }else{
            if(!cus.getPassword().equals(c.getPassword())){
                model.addAttribute("message","Sai mật khẩu");
                return "user/login";
            }else{
                model.addAttribute("message","Đăng nhập thành công");
                return "user/login";
            }
        }
    }

    @GetMapping("/customer/forgotpass")
    public String showForgotPassPage(Model model){
        model.addAttribute("customer",new ForgotPassDto());
        return "user/forgotpass";
    }
    @PostMapping("/customer/forgotpass/send")
    public String processForgotPass(@ModelAttribute("customer") ForgotPassDto f , Model model){
        Customer cus = customerService.findByEmail(f.getEmail());
        if(cus==null){
            model.addAttribute("message","Không tìm thấy email");
            return "user/forgotpass";
        }else{
            if(!f.getNewPassword().equals(f.getConfirmPassword())){
                model.addAttribute("message","Mật khẩu nhập lại không đúng");
                return "user/forgotpass";
            }else{
                cus.setPassword(f.getNewPassword());
                customerService.add_update(cus);
                model.addAttribute("message","Đổi mật khẩu thành công");
                return "user/forgotpass";
            }
        }
    }
}
