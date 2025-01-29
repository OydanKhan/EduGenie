package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.Questions;
import com.usyd.edugenie.entity.Quizzes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface QuestionsRepository extends JpaRepository<Questions, UUID> {
    // Retrieve all questions for a given quiz ID
    List<Questions> findByQuiz_QuizId(UUID quizId);

    // Retrieve a specific question by both quiz and questionId
    Optional<Questions> findByQuizAndQuestionId(Quizzes quiz, UUID questionId);
}
