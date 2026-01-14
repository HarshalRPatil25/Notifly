package com.notifly.backend.User.Service;

import java.util.Optional;

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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

      @Transactional
    public boolean updateProfile(User user){
        if(user==null){
            return false;
        }
        if(userRepository.existsByUsername(user.getUsername())){
            if(!userRepository.existsByEmail(user.getEmail())){
                if(!userRepository.existsByPhoneNumber(user.getPhoneNumber())){
                    Optional<User>exsitedUser=userRepository.findByUsername(user.getUsername());
                    if(exsitedUser.get()!=null){
                        exsitedUser.get().setPhoneNumber(user.getPhoneNumber());
                        exsitedUser.get().setEmail(user.getEmail());
                        userRepository.save(exsitedUser.get());

                    }
                }
            }
        }
        return false;
    }

public User getUserByUsername(String username){
    if(username==null||username.isEmpty())return null;

   if(userRepository.existsByUsername(username)){
    Optional<User>userProfile = userRepository.findByUsername(username);
    userProfile.get().setPassword(null);
    return userProfile.get();
   }
   return null;
}

}