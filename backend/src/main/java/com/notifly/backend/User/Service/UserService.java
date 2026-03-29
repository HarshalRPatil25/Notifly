package com.notifly.backend.User.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.notifly.backend.JobPreferences.Entity.JobPreference;

import com.notifly.backend.User.DTO.UserDTO;
import com.notifly.backend.User.DTO.UserProfileDTO;
import com.notifly.backend.User.Entity.CridentialVerfication;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;
import com.notifly.backend.Verification.OTP.OTPService;
import com.notifly.backend.Verification.OTP.SMTP.EmailService;
import com.notifly.backend.Verification.OTP.SMTP.OtpService;

import com.notifly.backend.Verification.Properties.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

 private final Logger log=LoggerFactory.getLogger(UserService.class);
  
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final OtpService mailOtpVerificationService;
    private final EmailService mailService;
    private final TwilioProperties props;
   
    // ================= REGISTER =================

public boolean saveUser(User user) {
    try {
        if (user == null) {
            log.warn("Attempted to save null user");
            return false;
        }

        log.info("Registering user username={} email={}",
                user.getUsername(), user.getEmail());

        CridentialVerfication verification = new CridentialVerfication();
        verification.setMailVerified(false);
        verification.setNumberVerified(false);
        verification.setUser(user);

        user.setCridentialVerfication(verification);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMailNotification(0L);
        user.setWhatsAppNotification(0L);
        userRepository.save(user);

        mailService.generateMailAfterUserRegistration(
                user.getEmail(), user.getUsername());

        log.info("User registered successfully username={}", user.getUsername());
        return true;

    } catch (Exception e) {
        log.error("Error while saving user username={} email={}",
                user != null ? user.getUsername() : "null",
                user != null ? user.getEmail() : "null",
                e);
        return false;
    }
}
    // ================= UPDATE PROFILE =================

  public boolean updateProfile(User user) {
    try {
        if (user == null || user.getUsername() == null) {
            log.warn("Invalid user update request");
            return false;
        }

        Optional<User> opt = userRepository.findByUsername(user.getUsername());
        if (opt.isEmpty()) {
            log.warn("User not found for username={}", user.getUsername());
            return false;
        }

        User currentUser = opt.get();
        CridentialVerfication verification = currentUser.getCridentialVerfication();

        log.debug("Updating profile for userId={}", currentUser.getId());

        // email update
        if (user.getEmail() != null &&
                !user.getEmail().equals(currentUser.getEmail()) &&
                !userRepository.existsByEmail(user.getEmail())) {

            currentUser.setEmail(user.getEmail());
            currentUser.setMailNotification(0L);
            verification.setMailVerified(false);
            log.info("Email updated for userId={}", currentUser.getId());
        }

        // phone update
        if (user.getPhoneNumber() != null &&
                !user.getPhoneNumber().equals(currentUser.getPhoneNumber()) &&
                !userRepository.existsByPhoneNumber(user.getPhoneNumber())) {

            currentUser.setPhoneNumber(user.getPhoneNumber());
            currentUser.setWhatsAppNotification(0L);
            verification.setNumberVerified(false);
            log.info("Phone updated for userId={}", currentUser.getId());
        }

        userRepository.save(currentUser);
        return true;

    } catch (Exception e) {
        log.error("Error updating profile username={}", 
                user != null ? user.getUsername() : "null", e);
        return false;
    }
}
    // ================= PROFILE =================

public UserProfileDTO getUserProfile(String username) {
    try {
        Optional<User> opt = userRepository.findByUsername(username);

        if (opt.isEmpty()) {
            log.warn("Profile fetch failed — user not found username={}", username);
            return null;
        }

        User user = opt.get();

        log.debug("Fetching profile for userId={}", user.getId());

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumnber(user.getPhoneNumber());
        dto.setPhoneVerified(user.getCridentialVerfication().isNumberVerified());
        dto.setMailVerified(user.getCridentialVerfication().isMailVerified());

        return dto;

    } catch (Exception e) {
        log.error("Error fetching profile username={}", username, e);
        return null;
    }
}

    // ================= PHONE OTP =================

public boolean verifyPhoneNumber(User user, String otp) {
    try {
        if (user == null || otp == null || otp.isBlank()) {
            log.warn("Invalid phone verification request");
            return false;
        }

        boolean verified = otpService.verifyOtp(user, otp);

        if (!verified) {
            log.warn("Phone OTP failed for userId={}", user.getId());
            return false;
        }

        user.getCridentialVerfication().setNumberVerified(true);
        userRepository.save(user);
        verificationConfirmation(user.getPhoneNumber());

        log.info("Phone verified for userId={}", user.getId());
        return true;

    } catch (Exception e) {
        log.error("Error verifying phone for userId={}",
                user != null ? user.getId() : null, e);
        return false;
    }
}

public boolean verificationConfirmation(String mobile){
      
        Message msg=Message.creator(
            new PhoneNumber("whatsapp:+91" + mobile),
            new PhoneNumber(props.getWhatsappFrom()),
            "Your Mobile Number  is verified"
        ).create();

        if(msg!=null){
            return true;
        }
        return false;


    }

    // ================= EMAIL OTP =================

    public boolean mailVerification(User user, String otp) {

        if (user == null || otp == null || otp.isBlank())
            return false;

        boolean verified =
                mailOtpVerificationService.verifyOtp(user.getEmail(), otp);

        if (!verified) return false;

        user.getCridentialVerfication().setMailVerified(true);
        userRepository.save(user);

        return true;
    }

@Transactional
public UserDTO userJobPreferencesAdded(Integer userId, List<JobPreference> titles) {

    try {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found userId={}", userId);
                    return new RuntimeException("User not found");
                });

        log.info("Adding {} job preferences to userId={}", titles.size(), userId);

        for (JobPreference incoming : titles) {
            JobPreference jp = new JobPreference();
            jp.setJobTitle(incoming.getJobTitle());
            jp.setUser(user);
            user.getJobPreference().add(jp);
        }

        userRepository.save(user);

        log.info("Job preferences added successfully userId={}", userId);
        return buildUserDTO(user);

    } catch (Exception e) {
        log.error("Error adding job preferences userId={}", userId, e);
        throw e;
    }
}


@Transactional
public UserDTO getUserJobPreferences(Integer userId) {

    try {
        User user = userRepository.getUserWithJobPreferences(userId)
                .orElseThrow(() -> {
                    log.error("User not found userId={}", userId);
                    return new RuntimeException("User not found");
                });

        log.info("Fetched {} job preferences for userId={}",
                user.getJobPreference().size(), userId);

        return buildUserDTO(user);

    } catch (Exception e) {
        log.error("Error fetching job preferences userId={}", userId, e);
        throw e;
    }
}

private UserDTO buildUserDTO(User user) {
    UserDTO dto = new UserDTO();
    dto.setUsername(user.getUsername());

    List<String> list = user.getJobPreference()
                            .stream()
                            .map(JobPreference::getJobTitle)
                            .toList();

    dto.setJobPreferences(list);
    return dto;
}




public List<String> allJobTitlesFromUsers() {
    try {
        List<User> userList = userRepository.findAll();
        List<String> jobTitles = new ArrayList<>();

        for (User user : userList) {
            for (JobPreference jobs : user.getJobPreference()) {
                jobTitles.add(jobs.getJobTitle());
            }
        }

        log.info("Collected {} job titles from {} users",
                jobTitles.size(), userList.size());

        return jobTitles;

    } catch (Exception e) {
        log.error("Error fetching all job titles", e);
        return new ArrayList<>();
    }
}
  

}
