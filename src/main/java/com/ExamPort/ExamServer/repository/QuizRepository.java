package com.ExamPort.ExamServer.repository;

import com.ExamPort.ExamServer.Entities.exam.Category;
import com.ExamPort.ExamServer.Entities.exam.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    public List<Quiz> findBycategory(Category category);

    public List<Quiz> findByActive(Boolean b);

    public List<Quiz> findByCategoryAndActive(Category c, Boolean b);
}
