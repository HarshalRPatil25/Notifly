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
import org.springframework.web.bind.annotation.RestController;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.User.Response.UserProfileRespose;
import com.notifly.backend.User.Service.UserService;
import com.notifly.backend.Verification.OTP.OTPService;
import com.notifly.backend.Verification.OTP.VerifyOtpRequest;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfile {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    @GetMapping
    public ResponseEntity<UserProfileRespose>getUserProfile(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated() && auth.getName()!=null){
            String username=auth.getName();
            User user=userService.getUserByUsername(username);
            if(user!=null && user.getUsername()!=null){
                return ResponseEntity.ok(new UserProfileRespose("200","User profile fetched successfully",user));

            }
            return ResponseEntity.status(404).body(new UserProfileRespose("404","User not found",null));

        }
        
        return ResponseEntity.status(401).body(new UserProfileRespose("401","Unauthorized",null));
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileRespose>updateUserProfile(User user){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated() && auth.getName()!=null & !auth.getName().isEmpty()){
            boolean isProfileUpdated=userService.updateProfile(user);
            if(isProfileUpdated){
                Optional<User>existedUserProfileUpdation=userRepository.findByUsername(user.getUsername());
                if(existedUserProfileUpdation.get()!=null){
                      return  ResponseEntity.status(200).body(new UserProfileRespose("200","User profile updated", user));

                }
                return  ResponseEntity.status(400).body(new UserProfileRespose("400","Bad request", user));

            }else{
            return  ResponseEntity.status(404).body(new UserProfileRespose("404","User not found",null));
            }

        }
        return ResponseEntity.status(401).body(new UserProfileRespose("401","Unauthorized",null));

    }

    // @PostMapping("/verify/phone")
    
      @PostMapping("/send/otp")
    public ResponseEntity<String> sendOtp() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated() && auth.getName()!=null){
            String username=auth.getName();
            Optional<User> currentUser=userRepository.findByUsername(username);
            if(currentUser!=null){
                otpService.sendOtp(currentUser.get().getPhoneNumber());
                  return ResponseEntity.ok("OTP sent via WhatsApp");
            }
                        return  ResponseEntity.status(404).body("User not found");

            
             
      
        }
                return ResponseEntity.status(401).body("Unauthorized");


       
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest req) {

        boolean verified = otpService.verifyOtp(
            req.getMobile(), req.getOtp());

        if (verified)
            return ResponseEntity.ok("Mobile verified successfully");

        return ResponseEntity.badRequest()
            .body("Invalid or expired OTP");
    }




}