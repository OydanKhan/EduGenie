package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsTest {

    private Questions question;
    private Quizzes quiz;

    @BeforeEach
    void setUp() {
        quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());

        question = new Questions();
        question.setQuestionId(UUID.randomUUID());
        question.setQuiz(quiz);
        question.setQuestionText("What is the capital of France?");
        question.setCorrectAnswer("Paris");
        question.setOptions(Arrays.asList("Berlin", "Madrid", "Paris", "Rome"));
    }

    @Test
    void testParameterizedConstructor() {
        Questions paramQuestion = new Questions(quiz, "What is the largest planet?", "Jupiter");
        assertEquals(quiz, paramQuestion.getQuiz());
        assertEquals("What is the largest planet?", paramQuestion.getQuestionText());
        assertEquals("Jupiter", paramQuestion.getCorrectAnswer());
    }

    @Test
    void testGettersAndSetters() {
        UUID questionId = UUID.randomUUID();
        question.setQuestionId(questionId);
        question.setQuestionText("What is the capital of Japan?");
        question.setCorrectAnswer("Tokyo");
        List<String> options = Arrays.asList("Osaka", "Tokyo", "Kyoto", "Nagoya");
        question.setOptions(options);

        assertEquals(questionId, question.getQuestionId());
        assertEquals("What is the capital of Japan?", question.getQuestionText());
        assertEquals("Tokyo", question.getCorrectAnswer());
        assertEquals(options, question.getOptions());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID questionId = UUID.randomUUID();
        question.setQuestionId(questionId);

        Questions anotherQuestion = new Questions();
        anotherQuestion.setQuestionId(questionId);

        assertEquals(question, anotherQuestion);
        assertEquals(question.hashCode(), anotherQuestion.hashCode());
    }

    @Test
    void testNotEquals() {
        Questions differentQuestion = new Questions();
        differentQuestion.setQuestionId(UUID.randomUUID());

        assertNotEquals(question, differentQuestion);
        assertNotEquals(question.hashCode(), differentQuestion.hashCode());
    }

    @Test
    void testToString() {
        String toStringResult = question.toString();
        assertTrue(toStringResult.contains("questionId="));
        assertTrue(toStringResult.contains("questionText='What is the capital of France?'"));
    }
}
