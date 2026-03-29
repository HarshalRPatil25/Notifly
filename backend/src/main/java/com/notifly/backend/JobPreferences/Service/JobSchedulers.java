package com.notifly.backend.JobPreferences.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.notifly.backend.JobPreferences.Entity.Job;
import com.notifly.backend.JobPreferences.Repository.JobRepo;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobSchedulers {

    private final Logger log=LoggerFactory.getLogger(JobSchedulers.class);


    private final UserService userService;
    private final JobRepo jobRepo;
    private final JobScappingService jobScappingService;

    private final JobService jobService;
   @Scheduled(cron = "0 5 0 * * ?", zone = "Asia/Kolkata")
   //@Scheduled(cron = "sec min hr/in24hrewatch * * ?", zone = "Asia/Kolkata")
       
    public void jobSapper(){
    try{

        log.info("jobService called at {}", LocalDateTime.now());
        ArrayList<Job>result=new ArrayList<>();
        List<String>list1=userService.allJobTitlesFromUsers();
      
        List<Job>job=jobScappingService.fetchJobRemotive(list1);
        List<Job>jobs=jobScappingService.fetchJobfindwork(list1);
        result.addAll(job);
        result.addAll(jobs);
        if(result.size()==0){
                
                log.info("No Job found");
        }
        log.info("Job scraped successfully.");

        jobRepo.saveAll(result);
         log.info("Jobs Saved in database successfully.");


    }
    catch(Exception e){
         log.error("Exception occured {}",e.getMessage());
    }
        
          
           
          
    
       
    }
    

    @Scheduled(cron = "0 1 0 * * ?", zone = "Asia/Kolkata")
    private void removeJobs(){
        try{
            log.info("Scheduler called for removing jobs {}",LocalDateTime.now());
            jobRepo.deleteAll();

        }
        catch(Exception e){
             log.error("Exception occured {}",e.getMessage());
        }
    }

  
    
}
