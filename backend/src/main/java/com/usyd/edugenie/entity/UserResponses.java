package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "user_responses")
public class UserResponses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID responseId;

    @ManyToOne
    @JoinColumn(name = "quizId", nullable = false)
    private Quizzes quiz;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private Questions question;

    @Column(length = 255, nullable = false)
    private String selectedAnswer;

    // Default constructor
    public UserResponses() {}

    // Parameterized constructor
    public UserResponses(Quizzes quiz, Questions question, String selectedAnswer) {
        this.quiz = quiz;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
    }

    // Getters and setters
    public UUID getResponseId() {
        return responseId;
    }

    public void setResponseId(UUID responseId) {
        this.responseId = responseId;
    }

    public Quizzes getQuiz() {
        return quiz;
    }

    public void setQuiz(Quizzes quiz) {
        this.quiz = quiz;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponses that = (UserResponses) o;
        return Objects.equals(responseId, that.responseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseId);
    }

    // toString()
    @Override
    public String toString() {
        return "UserResponses{" +
            "responseId=" + responseId +
            ", selectedAnswer='" + selectedAnswer + '\'' +
            '}';
    }
}
