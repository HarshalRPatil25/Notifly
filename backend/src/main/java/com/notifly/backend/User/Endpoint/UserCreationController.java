package com.notifly.backend.User.Endpoint;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notifly.backend.Security.UserDetailsServiceCustom;
import com.notifly.backend.Security.JWT.JWTService;
import com.notifly.backend.User.Entity.Login;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.User.Response.ServiceRespose;
import com.notifly.backend.User.Service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/public")
public class UserCreationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @Autowired
    private AuthenticationManager authenticationManager;


  @PostMapping("/register")
public ResponseEntity<ServiceRespose> registerUser(
        @Valid @RequestBody User user,
        BindingResult result
) {

    if (result.hasErrors()) {
        List<String> errors = result.getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              
                .body(new ServiceRespose("400", "Validation failed *"+errors,null));
    }

    if (userRepository.existsByUsername(user.getUsername())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ServiceRespose("409", "Username already exists", null));
    }

    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ServiceRespose("409", "Email already exists", null));
    }

    if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ServiceRespose("409", "Phone number already exists", null));
    }

    userService.saveUser(user);

    UserDetails userDetails =
            userDetailsServiceCustom.loadUserByUsername(user.getUsername());

    String token = jwtService.generateToken(userDetails);

    return ResponseEntity.ok(
            new ServiceRespose("200", "User created successfully", token)
    );
}

    @PostMapping("/login")
    public ResponseEntity<ServiceRespose> login(@Valid @RequestBody Login request, BindingResult result) {

      
        if(result.hasErrors()){
                List<String>errors=result.getFieldErrors().stream().map(e->e.getField()+": "+e.getDefaultMessage()).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceRespose("400","Validation failed *"+errors,null));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
                    
            if (authentication.isAuthenticated()) {
                UserDetails userDetails =
                        userDetailsServiceCustom.loadUserByUsername(request.getUsername());

                String jwt = jwtService.generateToken(userDetails);
                
                return new ResponseEntity<>(new ServiceRespose("200", "Login successful", jwt), HttpStatus.OK);
            }
            
        } catch (AuthenticationException e) {
            // Invalid credentials
            return new ResponseEntity<>(new ServiceRespose("401", "Invalid username or password", null), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new ServiceRespose("401", "Authentication failed", null), HttpStatus.UNAUTHORIZED);
    }


}
