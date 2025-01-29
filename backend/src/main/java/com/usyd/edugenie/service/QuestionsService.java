package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.Questions;
import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.UserResponses;
import com.usyd.edugenie.repository.QuestionsRepository;
import com.usyd.edugenie.repository.UserResponsesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionsService {

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private UserResponsesRepository UserResponsesRepository;

    public Questions saveQuestion(Questions question) {

        return questionsRepository.save(question);
    }
    public List<Questions> getQuestionsByQuizId(UUID quizId) {
        return questionsRepository.findByQuiz_QuizId(quizId);
    }

    public void updateUserAnswer(Quizzes quiz, UUID questionId, String userAnswer) {
        // Retrieve the question by quiz and questionId
        Optional<Questions> questionOptional = questionsRepository.findByQuizAndQuestionId(quiz, questionId);

        if (questionOptional.isPresent()) {
            Questions question = questionOptional.get();

            // Check if the user response for this question already exists
            Optional<UserResponses> existingResponse = UserResponsesRepository.findByQuizAndQuestion(quiz, question);

            UserResponses userResponse;
            if (existingResponse.isPresent()) {
                // Update the existing response
                userResponse = existingResponse.get();
                userResponse.setSelectedAnswer(userAnswer);
            } else {
                // Create a new user response if none exists
                userResponse = new UserResponses(quiz, question, userAnswer);
            }

            // Save the updated or new user response
            UserResponsesRepository.save(userResponse);
        }
    }

}
