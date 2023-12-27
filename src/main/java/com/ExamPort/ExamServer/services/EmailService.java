package com.ExamPort.ExamServer.services;

public interface EmailService {

    public void sendResetEmail(String to, String resetToken);
}
