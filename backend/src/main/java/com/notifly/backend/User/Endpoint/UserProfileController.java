package com.notifly.backend.User.Endpoint;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.User.Response.UserProfileRespose;
import com.notifly.backend.User.Service.UserService;
import com.notifly.backend.Verification.OTP.OTPService;
import com.notifly.backend.Verification.OTP.VerifyOtpRequest;
import com.notifly.backend.Verification.OTP.SMTP.OtpService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;
    private final OTPService otpService;
    private final UserService userService;
    private final OtpService smtpOTPService;

    // ================= AUTH =================

    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return auth.getName();
    }

    private User getCurrentUser() {
        String username = getUsername();
        if (username == null) return null;

        return userRepository.findByUsername(username).orElse(null);
    }

    // ================= PROFILE =================

    @GetMapping
    public ResponseEntity<?> getProfile() {
        User user = getCurrentUser();
        if (user == null)
            return ResponseEntity.status(401).body("Unauthorized");

        return ResponseEntity.ok(
                new UserProfileRespose("200", "Profile fetched successfully", user)
        );
    }

    // ================= PHONE OTP =================

    @PostMapping("/otp/phone/send")
    public ResponseEntity<String> sendPhoneOtp() {
        User user = getCurrentUser();
        if (user == null)
            return ResponseEntity.status(401).body("Unauthorized");

        otpService.sendOtp(user.getPhoneNumber());
        return ResponseEntity.ok("OTP sent to mobile");
    }

    @PostMapping("/otp/phone/verify")
    public ResponseEntity<String> verifyPhoneOtp(
            @RequestParam String otp) {

        User user = getCurrentUser();
        if (user == null)
            return ResponseEntity.status(401).body("Unauthorized");

        boolean verified = otpService.verifyOtp(
                user.getPhoneNumber(), otp);

        if (!verified)
            return ResponseEntity.badRequest().body("Invalid or expired OTP");

        return ResponseEntity.ok("Phone verified successfully");
    }

    // ================= EMAIL OTP =================

    @PostMapping("/otp/email/send")
    public ResponseEntity<String> sendEmailOtp() {
        User user = getCurrentUser();
        if (user == null)
            return ResponseEntity.status(401).body("Unauthorized");

        smtpOTPService.sendOtp(user.getEmail());
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/otp/email/verify")
    public ResponseEntity<String> verifyEmailOtp(
            @RequestParam String otp) {

        User user = getCurrentUser();
        if (user == null)
            return ResponseEntity.status(401).body("Unauthorized");

        boolean verified=userService.mailVerification(user,otp);

        if (!verified)
            return ResponseEntity.badRequest().body("Invalid or expired OTP");

        
        return ResponseEntity.ok("Email verified successfully");
    }
}
