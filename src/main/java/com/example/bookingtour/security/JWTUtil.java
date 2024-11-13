package com.example.bookingtour.security;

import com.example.bookingtour.entity.Customer;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.Console;
import java.text.ParseException;
import java.util.Date;

public class JWTUtil {
    // Khóa bí mật dùng để ký và xác minh JWT
    private static final String SECRET_KEY = "your-very-secure-secret-key-that-is-long-enough-256-bits!";
    // Tạo JWT từ thông tin người dùng (Customer)
    public static String createJWT(Customer customer) throws JOSEException {
        // Tạo JWT Claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(customer.getEmail())  // Lưu email vào subject
                .claim("name", customer.getName())  // Lưu tên người dùng
                .claim("id", customer.getCustomer_id())
                .claim("email",customer.getEmail())// Lưu vai trò (nếu có)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 864_000_000)) // 10 ngày
                .build();

        // Ký JWT với khóa bí mật
        JWSSigner signer = new MACSigner(SECRET_KEY);
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer);

        // Trả về JWT đã ký dưới dạng chuỗi
        return signedJWT.serialize();
    }
    // Giải mã và xác minh JWT
    public static String parseJWT(String token) throws JOSEException, ParseException {
        // Phân tích JWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Xác minh chữ ký của JWT
        JWSVerifier verifier = new MACVerifier(SECRET_KEY);
        if (signedJWT.verify(verifier)) {
            // Trả về email trong claims của JWT
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getSubject(); // Trả về email của người dùng
        } else {
            throw new JOSEException("Invalid signature");
        }
    }
    public static String getCustomerIdFromJWT(String jwtToken) throws JOSEException, ParseException {
        // Giải mã JWT
        SignedJWT signedJWT = SignedJWT.parse(jwtToken);

        // Kiểm tra tính hợp lệ của JWT (bằng cách xác minh chữ ký với khóa bí mật)
        MACVerifier verifier = new MACVerifier(SECRET_KEY); // Dùng SECRET_KEY để xác minh chữ ký
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature");
        }

        // Lấy JWT Claims
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        // Trích xuất giá trị của claim "id" từ JWT
        String customerId = claims.getStringClaim("id");

        return customerId;
    }
    public static boolean validateJWT(String jwtToken) {
        try {
            // Parse JWT token
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);

            // Verify the token signature using the secret key
            MACVerifier verifier = new MACVerifier(SECRET_KEY);
            if (!signedJWT.verify(verifier)) {
                System.out.println("Invalid JWT signature");
                return false;
            }

            // Get claims from the token
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Check if the token has expired
            Date expirationTime = claims.getExpirationTime();
            if (new Date().after(expirationTime)) {
                System.out.println("JWT token has expired");
                return false;
            }

            // Optional: Retrieve claims for additional verification or use
            String userId = claims.getStringClaim("id");
            String email = claims.getStringClaim("email");
            System.out.println("Token is valid. User ID: " + userId + ", Email: " + email);

            return true;  // Token is valid and signature is verified
        } catch (JOSEException | java.text.ParseException e) {
            System.out.println("Error validating JWT: " + e.getMessage());
            return false;
        }
    }
}
