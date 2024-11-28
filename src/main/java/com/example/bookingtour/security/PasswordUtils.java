package com.example.bookingtour.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Đăng ký lớp như một Bean
public class PasswordUtils {

    private final PasswordEncoder passwordEncoder;

    public PasswordUtils() {
        this.passwordEncoder = new BCryptPasswordEncoder(); // Khởi tạo BCryptPasswordEncoder
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password); // Trả về mật khẩu đã mã hóa
    }
}
