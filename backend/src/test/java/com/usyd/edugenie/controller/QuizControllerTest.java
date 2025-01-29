package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Questions;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.model.QuizGenerateReq;
import com.usyd.edugenie.service.QuizzesService;
import com.usyd.edugenie.service.QuestionsService;
import com.usyd.edugenie.service.UserGeneratedStudyNotesService;
import com.usyd.edugenie.repository.UsersRepository;
import com.usyd.edugenie.model.TagScore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class QuizControllerTest {

    @InjectMocks
    private QuizController quizController;

    @Mock
    private QuizzesService quizzesService;

    @Mock
    private QuestionsService questionsService;

    @Mock
    private UserGeneratedStudyNotesService studyNotesService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Users mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        mockUser = new Users();
        mockUser.setUserId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    public void testSubmitQuiz() {
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());
        quiz.setScore(80);
        quiz.setTotalQuestions(10);

        List<Quizzes> quizzesList = Collections.singletonList(quiz);
        when(quizzesService.getQuizzesByUserSortedByDate(any())).thenReturn(quizzesList);

        ResponseEntity<String> response = quizController.submitQuiz(quiz.getScore());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Quiz submitted successfully. Your score is: " + quiz.getScore(), response.getBody());
    }

    @Test
    public void testGetQuizById() {
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());
        quiz.setTopic("Java");

        Questions question = new Questions();
        question.setQuestionText("What is Java?");
        List<Questions> questionsList = Collections.singletonList(question);

        when(quizzesService.getQuizById(quiz.getQuizId())).thenReturn(Optional.of(quiz));
        when(questionsService.getQuestionsByQuizId(quiz.getQuizId())).thenReturn(questionsList);

        ResponseEntity<Map<String, Object>> response = quizController.getQuizById(quiz.getQuizId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Java", ((Quizzes) response.getBody().get("quiz")).getTopic());
    }

    @Test
    public void testGetTagScoresForUser() {
        TagScore tagScore = new TagScore();
        tagScore.setTag("Java");
        tagScore.setAvgScore(85);

        List<TagScore> tagScores = Collections.singletonList(tagScore);
        when(quizzesService.getTagScoreForUser(any())).thenReturn(tagScores);

        ResponseEntity<List<TagScore>> response = quizController.getTagScoresForUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(85, response.getBody().get(0).getAvgScore());
    }
}


