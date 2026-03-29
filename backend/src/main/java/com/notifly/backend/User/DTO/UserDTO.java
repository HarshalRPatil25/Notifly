package com.notifly.backend.User.DTO;

import java.util.List;

import com.notifly.backend.JobPreferences.Entity.JobPreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private List<String> jobPreferences;
}
