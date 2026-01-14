package com.notifly.backend.User.Response;

import com.notifly.backend.User.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class UserProfileRespose {

    private String statusCode;
    private String message;
    private User user;
    
}
