package com.ExamPort.ExamServer.repository;

import com.ExamPort.ExamServer.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

   public User findByUsername(String username);


   //for reset password
  public User findByEmail(String email);

  public User findByResetToken(String resetToken);
}
