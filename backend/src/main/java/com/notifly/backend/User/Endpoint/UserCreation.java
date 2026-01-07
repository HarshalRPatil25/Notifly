package com.notifly.backend.User.Endpoint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notifly.backend.Security.UserDetailsServiceCustom;
import com.notifly.backend.Security.JWT.JWTService;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Response.ServiceRespose;
import com.notifly.backend.User.Service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.userdetails.UserDetails;
@RestController
@RequestMapping("/api/public")
public class UserCreation {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @PostMapping("/register")
    public ServiceRespose registerUser(@RequestBody User user) {

        if (user == null) {
            return new ServiceRespose("400", "Invalid user data", null);
        }

        // Check username conflict first
        if (userService.userExists(user.getUsername())) {
            return new ServiceRespose("409", "Username already exists", null);
        }

        boolean isUserCreated = userService.saveUser(user);

        if (isUserCreated) {
            UserDetails userDetails =
                    userDetailsServiceCustom.loadUserByUsername(user.getUsername());

            String token = jwtService.generateToken(userDetails);
            return new ServiceRespose("200", "User created successfully", token);
        }

        return new ServiceRespose("500", "User creation failed", null);
    }
}
