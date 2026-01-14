package com.notifly.backend.Verification.OTP;

import java.security.SecureRandom;

public class OTPUtil {
    public static String generate() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}

    
