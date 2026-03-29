package com.notifly.backend.JobPreferences.Service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.notifly.backend.JobPreferences.Entity.Job;
import com.notifly.backend.JobPreferences.Repository.JobRepo;
import com.notifly.backend.MailFormat.Mailers;
import com.notifly.backend.Verification.Properties.TwilioProperties;


import com.notifly.backend.Verification.Properties.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final JavaMailSender mailSender;
    private final Mailers        mailers;
    private final JobRepo jobRepo;
    private final TwilioProperties props;

    public List<Job> allJobs() {
        logger.info("Fetching all jobs from database");
        return jobRepo.findAll();
    }

   
    public boolean sendHtmlMail(String to, String subject, String htmlBody) {
    try {
        logger.debug("Sending email to={} subject={}", to, subject);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);

        logger.debug("Email successfully sent to={}", to);
        return true;

    } catch (Exception e) {
        logger.error("Mail sending failed | to={} subject={}", to, subject, e);
        return false;
    }



}
    public String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }


     public List<Job> findMatchingJobs(List<String> keywords) {
    try {
        List<Job> matched = jobRepo.findBySearchKeywordInIgnoreCase(keywords);

        if (!matched.isEmpty()) {
            logger.debug("DB matched {} jobs for keywords={}", matched.size(), keywords);
            return matched;
        }

        logger.debug("Fallback to manual search for keywords={}", keywords);

        List<Job> fallback = new ArrayList<>();
        for (Job job : jobRepo.findAll()) {
            String title = job.getTitle() == null ? "" : job.getTitle().toLowerCase();
            for (String kw : keywords) {
                if (title.contains(kw)) {
                    fallback.add(job);
                    break;
                }
            }
        }

        logger.debug("Fallback matched {} jobs", fallback.size());
        return fallback;

    } catch (Exception e) {
        logger.error("Error while finding matching jobs for keywords={}", keywords, e);
        return new ArrayList<>();
    }
}


public boolean sendWhatsAppMessage(String mobile, String message) {
    try {
        if (mobile == null || mobile.isBlank()) {
            throw new IllegalArgumentException("Mobile number is empty!");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message is empty!");
        }

        logger.debug("Sending WhatsApp message to={}", mobile);

        Message.creator(
            new PhoneNumber("whatsapp:+91" + mobile),
            new PhoneNumber(props.getWhatsappFrom()),
            message
        ).create();

        logger.debug("WhatsApp message successfully sent to={}", mobile);
        return true;

    } catch (Exception e) {
        logger.error("WhatsApp sending failed | to={}", mobile, e);
        return false;
    }
}



}