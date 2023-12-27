package com.ExamPort.ExamServer.repository;

import com.ExamPort.ExamServer.Entities.exam.Question;
import com.ExamPort.ExamServer.Entities.exam.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Set<Question> findByQuiz(Quiz quiz);
}
