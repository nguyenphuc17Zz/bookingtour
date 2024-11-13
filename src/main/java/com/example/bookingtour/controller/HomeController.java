package com.example.bookingtour.controller;

import com.example.bookingtour.entity.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String showHomePage() {
        return "user/home";
    }
}
