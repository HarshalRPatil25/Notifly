package com.notifly.backend.Verification.OTP;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;
@Component
public class OTPUtil {
    public static String generate() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}

    
