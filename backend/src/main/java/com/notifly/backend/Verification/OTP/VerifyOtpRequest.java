package com.notifly.backend.Verification.OTP;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String mobile;
    private String otp;
}
