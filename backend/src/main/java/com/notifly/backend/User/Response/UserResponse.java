package com.notifly.backend.User.Response;

import com.notifly.backend.User.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String statusCode;
    private String meassage;
    private UserDTO user;
}
