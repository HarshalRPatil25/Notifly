package com.notifly.backend.Verification.OTP.SMTP;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class OtpHashUtil {

    public String hash(String otp) {
        return BCrypt.hashpw(otp, BCrypt.gensalt());
    }

    public boolean matches(String otp, String hash) {
        return BCrypt.checkpw(otp, hash);
    }
}
