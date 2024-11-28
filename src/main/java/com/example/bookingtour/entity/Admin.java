package com.example.bookingtour.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "admins")
public class Admin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int admin_id;
    private String admin_name;
    private String email;
    private String password;
    private String role;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Admin() {
    }

    public Admin(int admin_id, String admin_name, String email, String password, String role) {
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Phương thức của UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về quyền của Admin, sử dụng role để cấp quyền
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return this.email; // Dùng email làm tên đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Nếu bạn muốn hỗ trợ hết hạn tài khoản, trả về false khi tài khoản hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status; // Kiểm tra trạng thái tài khoản, nếu bị khóa trả về false
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Nếu bạn muốn hỗ trợ hết hạn mật khẩu, trả về false khi mật khẩu hết hạn
    }

    @Override
    public boolean isEnabled() {
        return this.status; // Kiểm tra xem tài khoản có đang hoạt động không
    }
}
