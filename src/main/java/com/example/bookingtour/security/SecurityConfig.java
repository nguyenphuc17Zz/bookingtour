package com.example.bookingtour.security;

import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.service.AdminService;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AdminService adminService;

    public SecurityConfig(AdminService adminService) {
        this.adminService = adminService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng BCrypt để mã hóa và giải mã mật khẩu
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(adminService)
                .passwordEncoder(passwordEncoder());  // Cấu hình sử dụng PasswordEncoder để so sánh mật khẩu;
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Tắt CSRF
                .authorizeRequests(auth -> auth
                        .requestMatchers("/admin/login", "/admin/login/send","/admin/forgotpass","/admin/forgotpass/send").permitAll() // Không cần đăng nhập cho những đường dẫn này
                        .requestMatchers("/admin/**").authenticated() // Cần đăng nhập cho các URL /admin/**
                )
                .formLogin(form -> form
                        .loginPage("/admin/login") // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/admin/login/send") // URL xử lý form đăng nhập
                        .usernameParameter("email") // Tên input email trong form
                        .passwordParameter("password") // Tên input password trong form
                        .successHandler((request, response, authentication) -> {
                            // Lấy role của người dùng đã đăng nhập
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            int adminId = ((Admin) authentication.getPrincipal()).getAdmin_id(); // Lấy adminId từ principal
                            String roles = ((Admin) authentication.getPrincipal()).getRole(); // Lấy adminId từ principal
                            System.out.println(roles);
                            System.out.println(adminId);
                            // Tạo cookie lưu role
                            Cookie roleCookie = new Cookie("role", role);
                            roleCookie.setPath("/");
                            roleCookie.setHttpOnly(false);
                            response.addCookie(roleCookie);

                            Cookie adminIdCookie = new Cookie("adminId", String.valueOf(adminId));
                            adminIdCookie.setPath("/");
                            adminIdCookie.setHttpOnly(false);
                            response.addCookie(adminIdCookie);

                            Cookie rolesCookie = new Cookie("roles", roles);
                            rolesCookie.setPath("/");
                            rolesCookie.setHttpOnly(false);
                            response.addCookie(rolesCookie);

                            // Chuyển hướng đến trang chính
                            response.sendRedirect("/admin/index");
                        })
                        .failureUrl("/admin/login?error=true") // Đăng nhập thất bại
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout") // Đường dẫn logout
                        .logoutSuccessUrl("/admin/login") // Chuyển hướng sau khi logout thành công
                        .invalidateHttpSession(true) // Hủy phiên
                        .deleteCookies("JSESSIONID", "adminId", "role","roles") // Xóa cookie JSESSIONID và adminId
                );
        return http.build();
    }


}
