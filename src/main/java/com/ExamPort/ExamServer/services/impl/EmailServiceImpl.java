package com.ExamPort.ExamServer.services.impl;

import com.ExamPort.ExamServer.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
     @Autowired
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset");
        message.setText("To reset your password, click the link below:\n\n" + "token will be expired after 10 minutes:\n\n" +
                "http://localhost:4200/complete-reset?token=" + resetToken);

        javaMailSender.send(message);
    }
}
