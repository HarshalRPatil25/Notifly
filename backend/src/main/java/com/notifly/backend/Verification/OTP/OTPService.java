package com.notifly.backend.Verification.OTP;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final RedisTemplate<String, String> redisTemplate;
    private final WhatsAppService whatsAppService;

    public void sendOtp(String mobile) {

        String otp = OTPUtil.generate();

        redisTemplate.opsForValue()
            .set("OTP:" + mobile, otp, 5, TimeUnit.MINUTES);

        whatsAppService.sendOtp(mobile, otp);
    }

    public boolean verifyOtp(String mobile, String inputOtp) {

        String key = "OTP:" + mobile;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp == null) return false;

        if (storedOtp.equals(inputOtp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
