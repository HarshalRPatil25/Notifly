package com.notifly.backend.Verification.OTP.SMTP;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.notifly.backend.Verification.OTP.OTPUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OTPUtil otpGenerator;
    private final OtpHashUtil otpHashUtil;
    private final EmailService emailService;

    @Value("${otp.expiry.minutes}")
    private long expiryMinutes;

    @Value("${otp.max.attempts}")
    private int maxAttempts;

    private static final String OTP_PREFIX = "OTP:";
    private static final String ATTEMPT_PREFIX = "OTP_ATTEMPT:";

    public void sendOtp(String email) {

        String otp = otpGenerator.generate();
        String otpHash = otpHashUtil.hash(otp);

        redisTemplate.opsForValue()
                .set(OTP_PREFIX + email, otpHash,
                        expiryMinutes, TimeUnit.MINUTES);

        redisTemplate.opsForValue()
                .set(ATTEMPT_PREFIX + email, "0",
                        expiryMinutes, TimeUnit.MINUTES);

        emailService.sendOtp(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {

        String otpKey = OTP_PREFIX + email;
        String attemptKey = ATTEMPT_PREFIX + email;

        String storedHash = redisTemplate.opsForValue().get(otpKey);

        if (storedHash == null)
            throw new RuntimeException("OTP expired or not found");

        int attempts = Integer.parseInt(
                redisTemplate.opsForValue().get(attemptKey)
        );

        if (attempts >= maxAttempts)
            throw new RuntimeException("Too many attempts");

        if (!otpHashUtil.matches(otp, storedHash)) {
            redisTemplate.opsForValue()
                    .increment(attemptKey);

            throw new RuntimeException("Invalid OTP");
        }

        // success
        redisTemplate.delete(otpKey);
        redisTemplate.delete(attemptKey);

        log.info("OTP verified for {}", email);
        return true;
    }
}
