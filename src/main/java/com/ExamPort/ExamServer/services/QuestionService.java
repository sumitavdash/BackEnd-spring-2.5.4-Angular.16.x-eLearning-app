package com.ExamPort.ExamServer.services;

import com.ExamPort.ExamServer.Entities.exam.Question;
import com.ExamPort.ExamServer.Entities.exam.Quiz;

import java.util.Set;

public interface QuestionService {

    public Question addQuestion(Question question);
    public Question updateQuestion(Question question);
    public Set<Question> getQuestions();
    public Question getQuestion(Long questionId);
    public Set<Question> getQuestionsOfQuiz(Quiz quiz);
    public void deleteQuestion(Long quesId);
    public Question get(Long questionsId);
}
