package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserResponsesTest {

    private UserResponses userResponse;
    private Quizzes quiz;
    private Questions question;

    @BeforeEach
    void setUp() {
        quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());

        question = new Questions();
        question.setQuestionId(UUID.randomUUID());

        userResponse = new UserResponses();
        userResponse.setResponseId(UUID.randomUUID());
        userResponse.setQuiz(quiz);
        userResponse.setQuestion(question);
        userResponse.setSelectedAnswer("A");
    }

    @Test
    void testParameterizedConstructor() {
        UserResponses paramUserResponse = new UserResponses(quiz, question, "B");

        assertEquals(quiz, paramUserResponse.getQuiz());
        assertEquals(question, paramUserResponse.getQuestion());
        assertEquals("B", paramUserResponse.getSelectedAnswer());
    }

    @Test
    void testGettersAndSetters() {
        UUID responseId = UUID.randomUUID();
        userResponse.setResponseId(responseId);
        userResponse.setSelectedAnswer("C");

        assertEquals(responseId, userResponse.getResponseId());
        assertEquals("C", userResponse.getSelectedAnswer());
        assertEquals(quiz, userResponse.getQuiz());
        assertEquals(question, userResponse.getQuestion());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID responseId = UUID.randomUUID();
        userResponse.setResponseId(responseId);

        UserResponses anotherUserResponse = new UserResponses();
        anotherUserResponse.setResponseId(responseId);

        assertEquals(userResponse, anotherUserResponse);
        assertEquals(userResponse.hashCode(), anotherUserResponse.hashCode());
    }

    @Test
    void testNotEquals() {
        UserResponses differentUserResponse = new UserResponses();
        differentUserResponse.setResponseId(UUID.randomUUID());

        assertNotEquals(userResponse, differentUserResponse);
        assertNotEquals(userResponse.hashCode(), differentUserResponse.hashCode());
    }

    @Test
    void testToString() {
        String toStringResult = userResponse.toString();
        assertTrue(toStringResult.contains("responseId="));
        assertTrue(toStringResult.contains("selectedAnswer='A'"));
    }
}

