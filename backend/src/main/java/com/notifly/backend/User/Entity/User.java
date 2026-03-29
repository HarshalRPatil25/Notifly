package com.notifly.backend.User.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.notifly.backend.JobPreferences.Entity.JobPreference;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "phone_number")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 10, max = 15)
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;


    @Column(name = "mail_notification_send",nullable =true)
    private Long mailNotification;


    @Column(name="whatsApp_notification_send",nullable =true)
     private Long whatsAppNotification;

 
    private LocalDate userCreationDate;
       @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)//inverse side
    private CridentialVerfication cridentialVerfication; 



@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<JobPreference> jobPreference=new ArrayList<>();


    @PrePersist
    public void prePersist() {
        this.userCreationDate = LocalDate.now();
    }
}
