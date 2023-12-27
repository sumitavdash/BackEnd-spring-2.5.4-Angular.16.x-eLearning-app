package com.ExamPort.ExamServer.services.impl;

import com.ExamPort.ExamServer.Entities.User;
import com.ExamPort.ExamServer.Entities.UserRole;
import com.ExamPort.ExamServer.repository.RoleRepository;
import com.ExamPort.ExamServer.repository.UserRepository;
import com.ExamPort.ExamServer.services.EmailService;
import com.ExamPort.ExamServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

//for reset password injecting password encoder

    //for reset password
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;


    //
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;

    //creating users
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("Already Existed USer !!");
            throw new Exception("User already present!!!");

        } else {
            //User create
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);
        }
        return local;
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(Long userId) {
        return this.userRepository.save(user);
    }



    @Override

    public User getUserById(Long userId) {

        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return  userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return  userRepository.findAll();
    }

    //for password reset
    @Override
    public void initiatePasswordReset(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("User with the provided email not found");
        }

        // Generate a reset token
        String resetToken = generateResetToken();

        // Set the reset token and expiry time
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10)); // 24 hours expiry

        // Save the user with the reset token
        userRepository.save(user);

        // Now, you can send an email to the user with a link containing the resetToken
        // The link can point to your frontend where the user can complete the password reset
        // You may want to use a service for sending emails.
        // Example: emailService.sendResetEmail(user.getEmail(), resetToken);
        emailService.sendResetEmail(user.getEmail(), resetToken);
    }

    // Utility method to generate a random reset token
    private String generateResetToken() {
        // Implement your logic to generate a unique reset token, for example, using UUID
        return UUID.randomUUID().toString();
    }


    @Override
    public void completePasswordReset(String token, String newPassword) throws Exception {
        User user = userRepository.findByResetToken(token);

        if (user == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new Exception("Invalid or expired reset token");
        }

        // Set the new password (you may want to use a password encoder)
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear the reset token and expiry time
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        // Save the user with the new password
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }






}