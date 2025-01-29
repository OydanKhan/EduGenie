package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuizOptionTest {

    @Test
    void testQuizOptionConstructorAndGetters() {
        // Create a QuizOption using the parameterized constructor
        QuizOption quizOption = new QuizOption("Programming language", true);

        // Verify that the values were correctly set using the constructor
        assertEquals("Programming language", quizOption.getOption());
        assertTrue(quizOption.isCorrect());
    }

    @Test
    void testSettersAndGetters() {
        // Create an empty QuizOption object
        QuizOption quizOption = new QuizOption();

        // Set values using setters
        quizOption.setOption("Animal");
        quizOption.setCorrect(false);

        // Verify that the values were correctly set using the setters
        assertEquals("Animal", quizOption.getOption());
        assertFalse(quizOption.isCorrect());
    }
}
