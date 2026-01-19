package com.library.seatmanager.controller;

import com.library.seatmanager.dto.LoginRequest;
import com.library.seatmanager.dto.OtpRequest;
import com.library.seatmanager.entity.Admin;
import com.library.seatmanager.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req,
                                        HttpSession session) {

        Admin admin = adminRepo.findByPhone(req.getPhone())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!encoder.matches(req.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate OTP (mock)
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        admin.setOtp(otp);
        admin.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        adminRepo.save(admin);

        // For now, log OTP (later SMS)
        System.out.println("OTP: " + otp);

        session.setAttribute("PHONE", admin.getPhone());

        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest req,
                                            HttpServletRequest request) {


        String phone = (String) request.getSession().getAttribute("PHONE");

        Admin admin = adminRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!admin.getOtp().equals(req.getOtp())
                || admin.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body("Invalid OTP");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        admin.getPhone(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );
        // Optional cleanup
        admin.setOtp(null);
        admin.setOtpExpiry(null);
        adminRepo.save(admin);

        return ResponseEntity.ok("Login success");
    }

    @GetMapping("/api/debug/auth")
    public String debugAuth(Authentication auth) {
        return auth == null ? "NOT_AUTHENTICATED" : auth.getName();
    }
}
