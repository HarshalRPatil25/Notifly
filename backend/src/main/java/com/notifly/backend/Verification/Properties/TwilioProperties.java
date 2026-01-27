package com.notifly.backend.Verification.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data

@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {
    private String accountSid;
    private String authToken;
    private String whatsappFrom;



}
