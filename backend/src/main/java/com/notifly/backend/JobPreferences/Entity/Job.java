package com.notifly.backend.JobPreferences.Entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String company;

    private String location;


    private String salary;

   
    private String jobUrl;

    private String source;

    // getters and setters

     @Column(name = "search_keyword")
    private String searchKeyword;
}