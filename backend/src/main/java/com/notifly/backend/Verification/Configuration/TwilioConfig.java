package com.notifly.backend.Verification.Configuration;

import org.springframework.context.annotation.Configuration;

import com.notifly.backend.Verification.Properties.TwilioProperties;
import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TwilioConfig {

    private final TwilioProperties props;

    @PostConstruct
    public void init() {
        Twilio.init(props.getAccountSid(), props.getAuthToken());
    }
}
