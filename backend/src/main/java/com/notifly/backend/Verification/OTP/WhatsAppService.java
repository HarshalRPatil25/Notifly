package com.notifly.backend.Verification.OTP;

import org.springframework.stereotype.Service;

import com.notifly.backend.Verification.Properties.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final TwilioProperties props;

   

    public void sendOtp(String mobile, String otp) {

        if (otp == null || otp.isBlank()) {
            throw new IllegalStateException("OTP is empty!");
        }

        Message.creator(
            new PhoneNumber("whatsapp:+91" + mobile),
            new PhoneNumber(props.getWhatsappFrom()),
            "Your OTP is " + otp + "\nValid for 5 minutes."
        ).create();
    }
}
