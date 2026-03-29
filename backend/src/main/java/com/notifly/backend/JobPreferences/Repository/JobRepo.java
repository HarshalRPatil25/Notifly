package com.notifly.backend.JobPreferences.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notifly.backend.JobPreferences.Entity.Job;

@Repository
public interface JobRepo extends JpaRepository<Job,Long>{



    List<Job> findBySearchKeywordInIgnoreCase(List<String> keywords);
 
    // Used by JobService — prevent duplicate scrapes
    boolean existsByJobUrl(String jobUrl);
}
