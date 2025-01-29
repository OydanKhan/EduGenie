package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuizzesTest {

    private Quizzes quizzes;
    private Users testUser;
    private StudyNotes testStudyNotes;
    private UserGeneratedStudyNotes testUserGeneratedNotes;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialize Validator for testing validation constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Initialize mock entities
        testUser = new Users();
        testUser.setUserId(UUID.randomUUID());

        testStudyNotes = new StudyNotes();
        testStudyNotes.setNoteId(UUID.randomUUID());

        testUserGeneratedNotes = new UserGeneratedStudyNotes();
        testUserGeneratedNotes.setUserNoteId(UUID.randomUUID());

        // Initialize Quizzes object
        quizzes = new Quizzes();
        quizzes.setQuizId(UUID.randomUUID());
        quizzes.setStudyNotes(testStudyNotes);
        quizzes.setUserGeneratedStudyNotes(testUserGeneratedNotes);
        quizzes.setUser(testUser);
        quizzes.setTopic("Sample Topic");
        quizzes.setGeneratedDate(LocalDateTime.now());
        quizzes.setTotalQuestions(10);
        quizzes.setLastAttemptDate(LocalDateTime.now().minusDays(1));
        quizzes.setScore(80);
        quizzes.setFeedback("Great job!");
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime generatedDate = LocalDateTime.now();
        LocalDateTime lastAttemptDate = generatedDate.minusDays(1);

        Quizzes newQuiz = new Quizzes(testStudyNotes, testUserGeneratedNotes, testUser, "New Topic", generatedDate, 10, lastAttemptDate, 90, "Well done!");

        assertEquals("New Topic", newQuiz.getTopic());
        assertEquals(generatedDate, newQuiz.getGeneratedDate());
        assertEquals(lastAttemptDate, newQuiz.getLastAttemptDate());
        assertEquals(10, newQuiz.getTotalQuestions());
        assertEquals(90, newQuiz.getScore());
        assertEquals("Well done!", newQuiz.getFeedback());
    }

    @Test
    void testGettersAndSetters() {
        UUID quizId = UUID.randomUUID();
        LocalDateTime generatedDate = LocalDateTime.now();
        LocalDateTime lastAttemptDate = generatedDate.minusDays(1);

        quizzes.setQuizId(quizId);
        quizzes.setTopic("Updated Topic");
        quizzes.setGeneratedDate(generatedDate);
        quizzes.setTotalQuestions(15);
        quizzes.setLastAttemptDate(lastAttemptDate);
        quizzes.setScore(85);
        quizzes.setFeedback("Keep it up!");

        assertEquals(quizId, quizzes.getQuizId());
        assertEquals("Updated Topic", quizzes.getTopic());
        assertEquals(generatedDate, quizzes.getGeneratedDate());
        assertEquals(15, quizzes.getTotalQuestions());
        assertEquals(lastAttemptDate, quizzes.getLastAttemptDate());
        assertEquals(85, quizzes.getScore());
        assertEquals("Keep it up!", quizzes.getFeedback());
    }

    @Test
    void testEqualsAndHashCode() {
        Quizzes anotherQuiz = new Quizzes();
        anotherQuiz.setQuizId(quizzes.getQuizId());

        assertEquals(quizzes, anotherQuiz);
        assertEquals(quizzes.hashCode(), anotherQuiz.hashCode());
    }

    @Test
    void testValidationConstraints() {
        quizzes.setTopic(null);  // Making topic null to check validation constraints

        Set<ConstraintViolation<Quizzes>> violations = validator.validate(quizzes);
        violations.forEach(v -> System.out.println("Violation on: " + v.getPropertyPath() + " - " + v.getMessage()));

        // Adjust the expected constraint violation count based on validations in the entity
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("topic")));
    }

    @Test
    void testToString() {
        String toStringResult = quizzes.toString();
        assertTrue(toStringResult.contains("quizId"));
        assertTrue(toStringResult.contains("topic=Sample Topic"));
        assertTrue(toStringResult.contains("totalQuestions=10"));
        assertTrue(toStringResult.contains("score=80"));
    }
}
