package com.notifly.backend.User.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public boolean saveUser(User user) {

        if (user == null) return false;

        if (userRepository.existsByUsername(user.getUsername())) {
            return false; // username conflict
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}

