package com.notifly.backend.User.Entity;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

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

    @Comment("Username of the user")
    private String username;

    @Comment("Password of the user")
    private String password;

}
