package com.notifly.backend.User.Entity;


import org.springframework.stereotype.Component;

import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component("Login DTO for user authentication")   
public class Login {

   @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(min = 6,max=18)
    @Column(nullable = false)
    private String password;


}
