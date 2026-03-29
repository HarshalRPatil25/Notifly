package com.notifly.backend.JobPreferences.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.notifly.backend.JobPreferences.Entity.Job;
@Service
public class JobScappingService {
      private static final Logger logger = LoggerFactory.getLogger(JobScappingService.class);

    
     public List<Job> fetchJobRemotive(List<String> jobTitles) {

        logger.info("Starting Remotive job fetch for titles: {}", jobTitles);

        RestTemplate restTemplate = new RestTemplate();
        List<Job> jobList = new ArrayList<>();
        Set<String> jobUrls = new HashSet<>();

        for (String str : jobTitles) {
            try {
                String url = "https://remotive.com/api/remote-jobs?search=" + str + "&limit=5";

                logger.info("Calling Remotive API for keyword: {}", str);

                Map response = restTemplate.getForObject(url, Map.class);

                if (response == null || response.get("jobs") == null) {
                    logger.warn("No jobs found for keyword: {}", str);
                    continue;
                }

                List<Map<String, Object>> jobsData =
                        (List<Map<String, Object>>) response.get("jobs");

                for (Map<String, Object> jobData : jobsData) {

                    String jobUrl = (String) jobData.get("url");

                    if (jobUrls.contains(jobUrl)) continue;

                    Job job = new Job();
                    job.setTitle((String) jobData.get("title"));
                    job.setCompany((String) jobData.get("company_name"));
                    job.setLocation((String) jobData.get("candidate_required_location"));
                    job.setSalary((String) jobData.get("salary"));
                    job.setJobUrl(jobUrl);
                    job.setSource("Remotive");

                    jobList.add(job);
                    jobUrls.add(jobUrl);
                }

            } catch (Exception e) {
                logger.error("Error fetching jobs from Remotive for keyword: {}", str, e);
            }
        }

        logger.info("Remotive job fetch completed. Total jobs fetched: {}", jobList.size());

        return jobList;
    }

    public List<Job> fetchJobfindwork(List<String> jobTitles) {

        logger.info("Starting Findwork job fetch for titles: {}", jobTitles);

        RestTemplate restTemplate = new RestTemplate();
        List<Job> jobList = new ArrayList<>();
        Set<String> jobUrls = new HashSet<>();

        for (String str : jobTitles) {
            try {
                String url = "https://remotive.com/api/remote-jobs?category=" + str;

                logger.info("Calling Findwork API for keyword: {}", str);

                Map response = restTemplate.getForObject(url, Map.class);

                if (response == null || response.get("jobs") == null) {
                    logger.warn("No jobs found for keyword: {}", str);
                    continue;
                }

                List<Map<String, Object>> jobsData =
                        (List<Map<String, Object>>) response.get("jobs");

                for (Map<String, Object> jobData : jobsData) {

                    String jobUrl = (String) jobData.get("url");

                    if (jobUrls.contains(jobUrl)) continue;

                    Job job = new Job();
                    job.setTitle((String) jobData.get("title"));
                    job.setCompany((String) jobData.get("company_name"));
                    job.setLocation((String) jobData.get("candidate_required_location"));
                    job.setSalary((String) jobData.get("salary"));
                    job.setJobUrl(jobUrl);
                    job.setSource("Remotive");

                    jobList.add(job);
                    jobUrls.add(jobUrl);
                }

            } catch (Exception e) {
                logger.error("Error fetching jobs from Findwork for keyword: {}", str, e);
            }
        }

        logger.info("Findwork job fetch completed. Total jobs fetched: {}", jobList.size());

        return jobList;
    }


}
