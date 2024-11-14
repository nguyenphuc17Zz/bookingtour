package com.example.bookingtour.controller;

import org.springframework.web.bind.annotation.GetMapping;
import com.example.bookingtour.entity.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LinkCustomerController {
    @GetMapping("/aboutus")
    public String aboutUs(Model model) {
        model.addAttribute("page", "aboutus");
        return "user/aboutus/aboutus";
    }

    @GetMapping("/service")
    public String service(Model model) {
        model.addAttribute("page", "service");
        return "user/service/service";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        model.addAttribute("page", "booking");
        return "user/booking/booking";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("page", "contact");
        return "user/contact/contact";
    }

    @GetMapping("/destination")
    public String destination(Model model) {
        model.addAttribute("page", "destination");
        return "user/destination/destination";
    }

    @GetMapping("/package")
    public String packagePage(Model model) {
        model.addAttribute("page", "package");
        return "user/package/package";
    }

    @GetMapping("/testimonial")
    public String testimonial(Model model) {
        model.addAttribute("page", "testimonial");
        return "user/testimonial/testimonial";
    }

    @GetMapping("/travelguides")
    public String travelguides(Model model) {
        model.addAttribute("page", "travelguides");
        return "user/travelguides/travelguides";
    }

}


