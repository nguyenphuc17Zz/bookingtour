package com.example.bookingtour.controller.user;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.security.JWTUtil;
import com.example.bookingtour.service.CustomerService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SignInGoogleController {
    @Autowired
    public CustomerService customerService;

    @GetMapping("/signingoogle")
    public String processJwt(OAuth2AuthenticationToken token) {


        String email = (String) token.getPrincipal().getAttributes().get("email");
        String name = (String) token.getPrincipal().getAttributes().get("name");

        Customer customer = customerService.findByEmail(email);
        if (customer == null) {
            customer = new Customer();
            customer.setEmail(email);
            customer.setName(name);
            customer.setStatus(true);
            customerService.save(customer);
        }

        if (!customer.isStatus()) {
            return "redirect:/login?error=account_locked";
        }

        try {
            String jwt = JWTUtil.createJWT(customer);

            return "redirect:/dashboard?jwt=" + jwt;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/login?error=jwt_creation_failed";
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dashboard")
    public String nothing(){
        return "user/authentication/nothing";
    }
}




