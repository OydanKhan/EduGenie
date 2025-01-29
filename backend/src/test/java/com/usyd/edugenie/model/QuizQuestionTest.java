package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizQuestionTest {

    @Test
    void testQuizQuestionConstructorAndGetters() {
        // Create QuizOption objects
        QuizOption option1 = new QuizOption("Programming language", true);
        QuizOption option2 = new QuizOption("Animal", false);
        List<QuizOption> options = Arrays.asList(option1, option2);

        // Create a QuizQuestion using the parameterized constructor
        QuizQuestion quizQuestion = new QuizQuestion("What is Java?", options);

        // Verify that the values were correctly set using the constructor
        assertEquals("What is Java?", quizQuestion.getText());
        assertEquals(2, quizQuestion.getOptions().size());
        assertEquals(option1, quizQuestion.getOptions().get(0));
        assertEquals(option2, quizQuestion.getOptions().get(1));
    }

    @Test
    void testSettersAndGetters() {
        // Create QuizOption objects
        QuizOption option1 = new QuizOption("Food", false);
        QuizOption option2 = new QuizOption("Drink", false);
        List<QuizOption> options = Arrays.asList(option1, option2);

        // Create an empty QuizQuestion object
        QuizQuestion quizQuestion = new QuizQuestion();

        // Set values using setters
        quizQuestion.setText("What is an apple?");
        quizQuestion.setOptions(options);

        // Verify that the values were correctly set using the setters
        assertEquals("What is an apple?", quizQuestion.getText());
        assertEquals(2, quizQuestion.getOptions().size());
        assertEquals(option1, quizQuestion.getOptions().get(0));
        assertEquals(option2, quizQuestion.getOptions().get(1));
    }
}