package com.notifly.backend.User.DTO;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UserProfileDTO {

    private String username;
    private String email;
    private String phoneNumnber;
    private boolean isMailVerified;
    private boolean isPhoneVerified;

    
}
