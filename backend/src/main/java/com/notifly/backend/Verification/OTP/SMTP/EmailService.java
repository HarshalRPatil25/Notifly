package com.notifly.backend.Verification.OTP.SMTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.notifly.backend.MailFormat.Mailers;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


    @Autowired
    private Mailers mailFormat;

   

    private final JavaMailSender mailSender;
    private Logger log=LoggerFactory.getLogger(EmailService.class);

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification");
        message.setText("""
                Your OTP: %s

                Valid for 5 minutes.
                Do not share this OTP.
                """.formatted(otp));

        mailSender.send(message);

        log.info("OTP email sent to {}", email);
    }

    public void generateMailAfterUserRegistration(String email, String userName) {
    try {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Welcome to Notifly "+userName);

        String mailBody = mailFormat.getMailAfterUserRegistration()
                .replace("{{userName}}", userName);

        helper.setText(mailBody, true); // true = HTML

        mailSender.send(mimeMessage);

    } catch (Exception e) {
        log.error("Mail sending failed: {}", e.getMessage());
    }
}

}
