package com.notifly.backend.Security;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceCustom.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        List<User> users = userRepository.findAllByUsername(username);

        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (users.size() > 1) {
            // Log a warning: database has duplicates for username
            logger.warn("Multiple users ({}) found with username='{}'. Using the first match.", users.size(), username);
        }

        User currentUser = users.get(0);

        return org.springframework.security.core.userdetails.User
                .withUsername(currentUser.getUsername())
                .password(currentUser.getPassword())
                .authorities(getAuthorities())
                .build();
    }

    private List<SimpleGrantedAuthority> getAuthorities() {
        // If you don’t have roles yet, keep default
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

  
}
