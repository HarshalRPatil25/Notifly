package com.notifly.backend.User.Entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "verification")
@Getter
@Setter

public class CridentialVerfication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isMailVerified = false;
    private boolean isNumberVerified = false;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) //owing
    private User user;
}
