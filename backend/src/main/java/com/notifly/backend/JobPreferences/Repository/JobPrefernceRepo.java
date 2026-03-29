package com.notifly.backend.JobPreferences.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notifly.backend.JobPreferences.Entity.JobPreference;

@Repository
public interface JobPrefernceRepo  extends JpaRepository<JobPreference,Integer>{
    
}
