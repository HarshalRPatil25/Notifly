package com.notifly.backend.JobPreferences.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notifly.backend.User.Entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_job_preferences")
@Data
public class JobPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name="work_location")
    private String location;

    @Column(name="Slary_Exepectation")
    private String salary;

    @Column(name="primary_coding_skill")
    private String primaryLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")   // FK column
    @JsonIgnore
    private User user;
}

