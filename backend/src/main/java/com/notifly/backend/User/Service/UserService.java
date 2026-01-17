package com.notifly.backend.User.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.Verification.OTP.OTPService;

import jakarta.transaction.Transactional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
      
    @Autowired
    private OTPService otpService;
    @Transactional
    public boolean saveUser(User user) {

        if (user == null) return false;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

      @Transactional
    public boolean updateProfile(User user){
         if(user!=null){
            Optional<User>currentUser=userRepository.findByUsername(user.getUsername());
            if(currentUser.get()!=null && currentUser.isEmpty()){
            if(!(userRepository.existsByEmail(user.getEmail()))){
                currentUser.get().setEmail(user.getEmail());;
                currentUser.get().setMailVerified(false);

            }
            if(!(userRepository.existsByPhoneNumber(user.getPhoneNumber()))){
                currentUser.get().setPhoneNumber(user.getPhoneNumber());
                currentUser.get().setMobileVerified(false);
             }
             userRepository.save(currentUser.get());
             return true;

          }
          return false;
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

public boolean verifyPhoneNumber(User user,String otp) {
    
    if(user!=null){
        if(otp!=null && !(otp.isEmpty())){
        boolean verified=otpService.verifyOtp(user.getPhoneNumber(), otp);
        if(verified){
            user.setMobileVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    
       }
       return false;
    }
    
    return false;

}



}