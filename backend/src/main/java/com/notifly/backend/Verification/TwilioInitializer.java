package com.notifly.backend.Verification;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.notifly.backend.Verification.Properties.TwilioProperties;
import com.twilio.Twilio;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TwilioInitializer {

    private final TwilioProperties props;

    @PostConstruct
    public void init() {
        Twilio.init(
            props.getAccountSid(),
            props.getAuthToken()
        );

        System.out.println("✅ Twilio initialized successfully");
    }
}
