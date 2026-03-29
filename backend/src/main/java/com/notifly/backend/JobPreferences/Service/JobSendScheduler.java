package com.notifly.backend.JobPreferences.Service;

import com.notifly.backend.JobPreferences.Entity.Job;
import com.notifly.backend.JobPreferences.Repository.JobRepo;
import com.notifly.backend.MailFormat.Mailers;
import com.notifly.backend.User.Entity.User;
import com.notifly.backend.User.Repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ← must be Spring's, not jakarta's

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSendScheduler {
    

    private final UserRepository userRepository;
    private final JobRepo  jobRepo;
    private final JobService jobService;
     private final JavaMailSender mailSender;
    private final Mailers        mailers;



    @Scheduled(cron = "0 2 22 * * ?", zone = "Asia/Kolkata")
@Transactional
public void sendJobAlertsMail() {

    long startTime = System.currentTimeMillis();
    log.info("=== MailScheduler started at {} ===", startTime);

    List<User> allUsers;
    try {
        allUsers = userRepository.findUsersWithJobPreference();
        log.info("Fetched {} users with job preferences", allUsers.size());
    } catch (Exception e) {
        log.error("CRITICAL: Failed to fetch users from DB", e);
        return; // stop execution if DB fails
    }

    AtomicInteger sent = new AtomicInteger(0);
    AtomicInteger skipped = new AtomicInteger(0);
    AtomicInteger failed = new AtomicInteger(0);

    for (User user : allUsers) {

        try {
            log.debug("Processing user: id={} email={}", user.getId(), user.getEmail());

            if (user.getCridentialVerfication() == null ||
                !user.getCridentialVerfication().isMailVerified()) {

                log.warn("Skipping userId={} — email not verified", user.getEmail());
                skipped.incrementAndGet();
                continue;
            }

            List<String> preferredTitles = user.getJobPreference()
                    .stream()
                    .map(jp -> jp.getJobTitle().toLowerCase())
                    .collect(Collectors.toList());

            if (preferredTitles.isEmpty()) {
                log.info("Skipping userId={} — no preferences set", user.getId());
                skipped.incrementAndGet();
                continue;
            }

            List<Job> matchedJobs = jobService.findMatchingJobs(preferredTitles);

            if (matchedJobs.isEmpty()) {
                log.info("No jobs found for userId={} | titles={}", user.getId(), preferredTitles);
                skipped.incrementAndGet();
                continue;
            }

            String prefLabel =preferredTitles.size() == 1
                    ? jobService.capitalize(preferredTitles.get(0))
                    : jobService.capitalize(preferredTitles.get(0)) + " + " + (preferredTitles.size() - 1) + " more";

            boolean success = jobService.sendHtmlMail(
                    user.getEmail(),
                    "🔔 " + matchedJobs.size() + " new jobs for you — " + prefLabel,
                    mailers.jobAlertEmail(user.getUsername(), matchedJobs, prefLabel)
            );

            if (success) {
                long currentMailCount=user.getMailNotification();
                user.setMailNotification(currentMailCount+1L);
                userRepository.save(user);
                sent.incrementAndGet();
                log.info("SUCCESS: Sent {} jobs → userId={} email={} total mail received={}",
                        matchedJobs.size(), user.getId(), user.getEmail(),user.getMailNotification());
            } else {
                failed.incrementAndGet();
                log.error("FAILED: Mail sending returned false → userId={}", user.getId());
            }

        } catch (Exception ex) {
            failed.incrementAndGet();
            log.error("ERROR processing userId={} email={}",
                    user.getId(), user.getEmail(), ex);
        }
    }

    long duration = System.currentTimeMillis() - startTime;

    log.info("=== MailScheduler finished | sent={} skipped={} failed={} | duration={}ms ===",
            sent.get(), skipped.get(), failed.get(), duration);
}

@Transactional
@Scheduled(cron = "0 20 0 * * ?", zone = "Asia/Kolkata")
public void sendJobAlertsWhatsapp() {

    long startTime = System.currentTimeMillis();
    log.info("=== MailScheduler started at {} ===", startTime);

    List<User> allUsers;
    try {
        allUsers = userRepository.findUsersWithJobPreference();
        log.info("Fetched {} users with job preferences", allUsers.size());
    } catch (Exception e) {
        log.error("CRITICAL: Failed to fetch users from DB", e);
        return; // stop execution if DB fails
    }

    AtomicInteger sent = new AtomicInteger(0);
    AtomicInteger skipped = new AtomicInteger(0);
    AtomicInteger failed = new AtomicInteger(0);

    for (User user : allUsers) {

        try {
            log.debug("Processing user: id={} email={}", user.getId(), user.getEmail());

            if (user.getCridentialVerfication() == null ||
                !user.getCridentialVerfication().isNumberVerified()) {

                log.warn("Skipping userId={} — WhatsApp Number not verified", user.getPhoneNumber());
                skipped.incrementAndGet();
                continue;
            }

            List<String> preferredTitles = user.getJobPreference()
                    .stream()
                    .map(jp -> jp.getJobTitle().toLowerCase())
                    .collect(Collectors.toList());

            if (preferredTitles.isEmpty()) {
                log.info("Skipping userId={} — no preferences set", user.getId());
                skipped.incrementAndGet();
                continue;
            }

            List<Job> matchedJobs = jobService.findMatchingJobs(preferredTitles);
            matchedJobs = matchedJobs.isEmpty() ? new ArrayList<>() : matchedJobs.subList(0, 1);

            if (matchedJobs.isEmpty()) {
                log.info("No jobs found for userId={} | titles={}", user.getId(), preferredTitles);
                skipped.incrementAndGet();
                continue;
            }

            String prefLabel = preferredTitles.size() == 1
                    ? jobService.capitalize(preferredTitles.get(0))
                    : jobService.capitalize(preferredTitles.get(0)) + " + " + (preferredTitles.size() - 1) + " more";

        
             
            boolean success=jobService.sendWhatsAppMessage(user.getPhoneNumber(),"🔔 " + matchedJobs.size() + 
                       mailers.jobAlertEmail(user.getUsername(), matchedJobs, prefLabel));



            if (success) {
                long currentMailCount=user.getMailNotification();
                user.setMailNotification(currentMailCount+1L);
                userRepository.save(user);
                sent.incrementAndGet();
                log.info("SUCCESS: Sent {} jobs → userId={} whatsAPP={} total mail received={}",
                        matchedJobs.size(), user.getId(), user.getPhoneNumber(),user.getWhatsAppNotification());
            } else {
                failed.incrementAndGet();
                log.error("FAILED: WhatsApp sending returned false → userId={}", user.getId());
            }

        } catch (Exception ex) {
            failed.incrementAndGet();
            log.error("ERROR processing userId={} email={}",
                    user.getId(), user.getPhoneNumber(), ex);
        }
    }

    long duration = System.currentTimeMillis() - startTime;

    log.info("=== WhatsApp Scheduler finished | sent={} skipped={} failed={} | duration={}ms ===",
            sent.get(), skipped.get(), failed.get(), duration);
}

 

}