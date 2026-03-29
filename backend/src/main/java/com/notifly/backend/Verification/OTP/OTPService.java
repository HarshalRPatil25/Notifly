package com.notifly.backend.Verification.OTP;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;

import lombok.RequiredArgsConstructor;




    
@Service
@RequiredArgsConstructor
public class OTPService {


    private final RedisTemplate<String, String> redisTemplate;
    private final WhatsAppService whatsAppService;
    private final UserRepository userRepo;
  



    public void sendOtp(String mobile) {

        String otp = OTPUtil.generate();

        redisTemplate.opsForValue()
            .set("OTP:" + mobile, otp, 5, TimeUnit.MINUTES);

        whatsAppService.sendOtp(mobile, otp);
    }

    public boolean verifyOtp(User user, String inputOtp) {
        if(user==null){
            return false;
        }
        
        
        String key = "OTP:" + user.getPhoneNumber();
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp == null) return false;

        if (storedOtp.equals(inputOtp)) {
            user.getCridentialVerfication().setNumberVerified(true);
            userRepo.save(user);
            redisTemplate.delete(key);
           
            return true;
        }
        return false;
    }
    
}
