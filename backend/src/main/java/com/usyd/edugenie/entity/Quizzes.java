package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull; // Import for validation
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Quizzes")
public class Quizzes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID quizId;

    @ManyToOne
    @JoinColumn(name = "noteId", nullable = true)
    private StudyNotes studyNotes;

    @ManyToOne
    @JoinColumn(name = "userNoteId", nullable = true)
    private UserGeneratedStudyNotes userGeneratedStudyNotes;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @NotNull // Ensure the topic cannot be null
    private String topic;

    private LocalDateTime generatedDate;
    private int totalQuestions;
    private LocalDateTime lastAttemptDate;
    private int score;

    @Column(length = 255)
    private String feedback;

    // Default constructor
    public Quizzes() {}

    // Parameterized constructor
    public Quizzes(StudyNotes studyNotes, UserGeneratedStudyNotes userGeneratedStudyNotes, Users user, String topic, LocalDateTime generatedDate, int totalQuestions, LocalDateTime lastAttemptDate, int score, String feedback) {
        this.studyNotes = studyNotes;
        this.userGeneratedStudyNotes = userGeneratedStudyNotes;
        this.user = user;
        this.topic = topic;
        this.generatedDate = generatedDate;
        this.totalQuestions = totalQuestions;
        this.lastAttemptDate = lastAttemptDate;
        this.score = score;
        this.feedback = feedback;
    }

    // Getters and setters
    public UUID getQuizId() {
        return quizId;
    }

    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }

    public StudyNotes getStudyNotes() {
        return studyNotes;
    }

    public void setStudyNotes(StudyNotes studyNotes) {
        this.studyNotes = studyNotes;
    }

    public UserGeneratedStudyNotes getUserGeneratedStudyNotes() {
        return userGeneratedStudyNotes;
    }

    public void setUserGeneratedStudyNotes(UserGeneratedStudyNotes userGeneratedStudyNotes) {
        this.userGeneratedStudyNotes = userGeneratedStudyNotes;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public LocalDateTime getLastAttemptDate() {
        return lastAttemptDate;
    }

    public void setLastAttemptDate(LocalDateTime lastAttemptDate) {
        this.lastAttemptDate = lastAttemptDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quizzes quizzes = (Quizzes) o;
        return Objects.equals(quizId, quizzes.quizId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId);
    }

    // toString()
    @Override
    public String toString() {
        return "Quizzes{" +
            "quizId=" + quizId +
            ", topic=" + topic +
            ", totalQuestions=" + totalQuestions +
            ", score=" + score +
            '}';
    }
}

