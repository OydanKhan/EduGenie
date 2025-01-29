package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class QuizGenerateReqTest {

    @Test
    void testQuizGenerateReqGettersAndSetters() {
        // Arrange
        String topic = "Science";
        String studyNotes = "Study notes for the quiz";
        int numberOfQuestions = 10;

        // Act
        QuizGenerateReq quizGenerateReq = new QuizGenerateReq();
        quizGenerateReq.setTopic(topic);
        quizGenerateReq.setStudyNotes(studyNotes);
        quizGenerateReq.setNumberOfQuestions(numberOfQuestions);

        // Assert
        assertThat(quizGenerateReq.getTopic()).isEqualTo(topic);
        assertThat(quizGenerateReq.getStudyNotes()).isEqualTo(studyNotes);
        assertThat(quizGenerateReq.getNumberOfQuestions()).isEqualTo(numberOfQuestions);
    }

    @Test
    void testToStringMethod() {
        // Arrange
        QuizGenerateReq quizGenerateReq = new QuizGenerateReq();
        quizGenerateReq.setTopic("Math");
        quizGenerateReq.setStudyNotes("Math study notes");
        quizGenerateReq.setNumberOfQuestions(5);

        // Act & Assert
        assertThat(quizGenerateReq.toString()).contains("Math", "Math study notes", "5");
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        QuizGenerateReq req1 = new QuizGenerateReq();
        req1.setTopic("History");
        req1.setStudyNotes("History study notes");
        req1.setNumberOfQuestions(8);

        QuizGenerateReq req2 = new QuizGenerateReq();
        req2.setTopic("History");
        req2.setStudyNotes("History study notes");
        req2.setNumberOfQuestions(8);

        // Act & Assert
        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());
    }
}
