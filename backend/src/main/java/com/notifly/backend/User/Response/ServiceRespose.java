package com.notifly.backend.User.Response;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceRespose {

    private String statusCode;
    private String message;
    private String token;
    
}
