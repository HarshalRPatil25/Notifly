package com.notifly.backend.User.Entity;

import java.time.LocalDate;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "username")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@NotBlank(message = "Username is mandatory" )
@Column(unique = true)
private String username;

@NotBlank(message = "Email is mandatory")
@Column(unique = true)
private String email;

@NotBlank(message = "Password is mandatory")
@Size(min = 6, message = "Password must be at least 6 characters long")
private String password;

@NotBlank(message = "PhoneNumber is mandatory")
@Column(unique = true)
private long phoneNumber;


private LocalDate userCreationDate;

    
}
