package com.notifly.backend.User.Endpoint;

import com.notifly.backend.JobPreferences.Service.JobService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.notifly.backend.Security.UserDetailsServiceCustom;
import com.notifly.backend.Security.JWT.JWTService;
import com.notifly.backend.User.Entity.Login;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.User.Response.ServiceRespose;
import com.notifly.backend.User.Service.UserService;
import com.notifly.backend.ratelimiter.RateLimiterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    private final JobService jobService;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final UserService userService;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final RateLimiterService rateLimiterService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<ServiceRespose> registerUser(
            @Valid @RequestBody User user,
            BindingResult result
    ) {
        try {
            logger.info("Register request received for username: {}", user.getUsername());

            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .toList();

                logger.warn("Validation failed for user: {} -> {}", user.getUsername(), errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ServiceRespose("400", "Validation failed *" + errors, null));
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                logger.warn("Username already exists: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ServiceRespose("409", "Username already exists", null));
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                logger.warn("Email already exists: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ServiceRespose("409", "Email already exists", null));
            }

            if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                logger.warn("Phone number already exists: {}", user.getPhoneNumber());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ServiceRespose("409", "Phone number already exists", null));
            }

            userService.saveUser(user);
            logger.info("User registered successfully: {}", user.getUsername());

            var userDetails = userDetailsServiceCustom.loadUserByUsername(user.getUsername());
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    new ServiceRespose("200", "User created successfully", token)
            );

        } catch (Exception e) {
            logger.error("Error during registration for user: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServiceRespose("500", "Something went wrong", null));
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<ServiceRespose> login(
            @Valid @RequestBody Login request,
            BindingResult result
    ) {

        String username = request.getUsername();

        try {
            logger.info("Login attempt for user: {}", username);

            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .toList();

                logger.warn("Validation failed for login user: {} -> {}", username, errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ServiceRespose("400", "Validation failed *" + errors, null));
            }

            // 🔐 Check if user is locked
            if (rateLimiterService.isKeyPresent(username)
                    && rateLimiterService.isNoAttempRemaining(username)) {

                logger.warn("User account locked due to max attempts: {}", username);

                return new ResponseEntity<>(
                        new ServiceRespose("423", "Account locked. Try again later.", null),
                        HttpStatus.LOCKED
                );
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            request.getPassword()
                    )
            );

            // ✅ SUCCESS
            if (authentication.isAuthenticated()) {
                logger.info("Login successful for user: {}", username);

                var userDetails = userDetailsServiceCustom.loadUserByUsername(username);
                String jwt = jwtService.generateToken(userDetails);

                return new ResponseEntity<>(
                        new ServiceRespose("200", "Login successful", jwt),
                        HttpStatus.OK
                );
            }

        } catch (Exception e) {
            logger.warn("Invalid login attempt for user: {}", username);

            try {
                // 🔻 Rate limiting logic
                if (rateLimiterService.isKeyPresent(username)) {

                    rateLimiterService.decreaseAttempt(username);

                    String attemptsRemaining =
                            rateLimiterService.remaingLoginAttempts(username);

                    logger.warn("Attempts remaining for user {}: {}", username, attemptsRemaining);

                    if (rateLimiterService.isNoAttempRemaining(username)) {
                        logger.error("User locked after max attempts: {}", username);

                        return new ResponseEntity<>(
                                new ServiceRespose("423", "Reached max login attempts", null),
                                HttpStatus.LOCKED
                        );
                    }

                    return new ResponseEntity<>(
                            new ServiceRespose("401",
                                    "Invalid username or password. Remaining attempts: " + attemptsRemaining,
                                    null),
                            HttpStatus.UNAUTHORIZED
                    );
                }

                // First failed attempt
                rateLimiterService.createKey(username);
                logger.warn("First failed login attempt recorded for user: {}", username);

            } catch (Exception ex) {
                logger.error("Rate limiter failure for user: {}", username, ex);
            }

            return new ResponseEntity<>(
                    new ServiceRespose("401", "Invalid username or password", null),
                    HttpStatus.UNAUTHORIZED
            );
        }

        return new ResponseEntity<>(
                new ServiceRespose("401", "Authentication failed", null),
                HttpStatus.UNAUTHORIZED
        );
    }
}