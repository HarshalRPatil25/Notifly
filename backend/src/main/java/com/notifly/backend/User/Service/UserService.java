package com.notifly.backend.User.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.notifly.backend.User.Entity.CridentialVerfication;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.Verification.OTP.OTPService;
import com.notifly.backend.Verification.OTP.SMTP.EmailService;
import com.notifly.backend.Verification.OTP.SMTP.OtpService;

import jakarta.transaction.Transactional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
      
    @Autowired
    private OTPService otpService;

    @Autowired 
    private OtpService mailOtpVarificationService;

    @Autowired
    private EmailService mailService;
  @Transactional
public boolean saveUser(User user) {

    if (user == null) return false;

    // create verification object
    CridentialVerfication verification = new CridentialVerfication();
    verification.setMailVerified(false);
    verification.setNumberVerified(false);

    // link both sides
    verification.setUser(user);
    
    user.setCridentialVerfication(verification);

    // encrypt password
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    
    mailService.generateMailAfterUserRegistration(user.getEmail(),user.getUsername());
    userRepository.save(user);

    return true;
}


     @Transactional
public boolean updateProfile(User user) {
    CridentialVerfication userCridentialVerifaction=new CridentialVerfication();

    if (user == null || user.getUsername() == null) return false;

    Optional<User> currentUserOpt =
            userRepository.findByUsername(user.getUsername());

    if (currentUserOpt.isEmpty()) return false;

    User currentUser = currentUserOpt.get();

    if (user.getEmail() != null &&
        !user.getEmail().equals(currentUser.getEmail()) &&
        !userRepository.existsByEmail(user.getEmail())) {

        currentUser.setEmail(user.getEmail());
        userCridentialVerifaction.setMailVerified(false);
        
    }

    if (user.getPhoneNumber() != null &&
        !user.getPhoneNumber().equals(currentUser.getPhoneNumber()) &&
        !userRepository.existsByPhoneNumber(user.getPhoneNumber())) {

        currentUser.setPhoneNumber(user.getPhoneNumber());
        userCridentialVerifaction.setNumberVerified(false);
    
    }
    
    currentUser.setCridentialVerfication(userCridentialVerifaction);
    userRepository.save(currentUser);
    return true;
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
CridentialVerfication userCurrentCridentialVerification=user.getCridentialVerfication();

    if(user!=null){
        if(otp!=null && !(otp.isEmpty())){
        boolean verified=otpService.verifyOtp(user.getPhoneNumber(), otp);
        if(verified){
           userCurrentCridentialVerification.setNumberVerified(true);
            user.setCridentialVerfication(userCurrentCridentialVerification);
            userRepository.save(user);
            return true;
        }
        return false;
    
       }
       return false;
    }
    
    return false;

}


public boolean mailVerification(User user,String otp){
    CridentialVerfication userCurrentCridentialVerification=user.getCridentialVerfication();
    if(user!=null){
        if(otp!=null && !(otp.isEmpty())){
        boolean verified=mailOtpVarificationService.verifyOtp(user.getEmail(), otp);
        if(verified){
            userCurrentCridentialVerification.setMailVerified(true);
            user.setCridentialVerfication(userCurrentCridentialVerification);
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