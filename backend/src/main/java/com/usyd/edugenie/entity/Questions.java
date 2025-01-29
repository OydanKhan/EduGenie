package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "Questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "quizId", nullable = false)
    private Quizzes quiz;

    @Column(length = 255, nullable = false)
    private String questionText;

    @Column(length = 255, nullable = false)
    private String correctAnswer;

    @ElementCollection   // list of options will be stored in a seperate table (but not its own entity)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options;

    // Default constructor
    public Questions() {}

    // Parameterized constructor
    public Questions(Quizzes quiz, String questionText, String correctAnswer) {
        this.quiz = quiz;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    // Getters and setters
    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public Quizzes getQuiz() {
        return quiz;
    }

    public void setQuiz(Quizzes quiz) {
        this.quiz = quiz;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questions that = (Questions) o;
        return Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }

    // toString()
    @Override
    public String toString() {
        return "Questions{" +
            "questionId=" + questionId +
            ", questionText='" + questionText + '\'' +
            '}';
    }
}
