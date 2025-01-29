package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.service.QuizzesService;
import com.usyd.edugenie.service.UsersService;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class QuizHistoryControllerTest {

    @InjectMocks
    private QuizHistoryController quizHistoryController;

    @Mock
    private QuizzesService quizzesService;

    @Mock
    private UsersService usersService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the SecurityContext with a mock user
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        Users mockUser = new Users();
        mockUser.setUserId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    public void testGetQuizzesForCurrentUser() {
        // Create a sample Quiz
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());
        quiz.setTopic("Java Basics");
        quiz.setGeneratedDate(LocalDateTime.now());
        quiz.setScore(85);

        // Mock service response for quizzes
        when(quizzesService.getQuizzesByUserSortedByDate(any())).thenReturn(Collections.singletonList(quiz));

        // Call the controller method
        ResponseEntity<List<Map<String, Object>>> response = quizHistoryController.getQuizzesForCurrentUser(null, null, null, null, null, null);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());  // Check that there's one quiz returned
    }

    @Test
    public void testGetTagsForCurrentUser() {
        // Create a sample Tag using the correct constructor
        Tag tag = new Tag(UUID.randomUUID(), "Java"); // Use the constructor with UUID and name

        // Mock service response for tags
        when(quizzesService.getTagsForUserQuizzes(any())).thenReturn(Set.of(tag.getName()));

        // Call the controller method
        ResponseEntity<List<String>> response = quizHistoryController.getTagsForCurrentUser();

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());  // Check that there's one tag returned
    }
}
