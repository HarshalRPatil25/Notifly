package com.notifly.backend.Verification.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data

@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {
    private String accountSid;
    private String authToken;
    private String whatsappFrom;


    @PostConstruct
public void checkEnv() {
    System.out.println("=== TWILIO CONFIG CHECK ===");
    System.out.println("SID = " + accountSid);
    System.out.println("TOKEN = " + (authToken != null ? "LOADED" : "NULL"));
    System.out.println("FROM = " + whatsappFrom);
}

}
