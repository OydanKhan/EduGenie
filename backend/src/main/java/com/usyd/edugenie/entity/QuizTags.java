package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "quiz_tags")
@IdClass(QuizTagsId.class)
public class QuizTags {

    @Id
    @Column(name = "quiz_id")
    private UUID quizId;

    @Id
    @Column(name = "tag_id")
    private UUID tagId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", insertable = false, updatable = false)
    private Quizzes quiz;

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;

    // Default constructor
    public QuizTags() {}

    public QuizTags(Quizzes quiz, Tag tag) {
        this.quiz = quiz;
        this.tag = tag;
        this.quizId = quiz.getQuizId();
        this.tagId = tag.getTagId();
    }

    // Getters and setters
    public UUID getQuizId() {
        return quizId;
    }

    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public Quizzes getQuiz() {
        return quiz;
    }

    public void setQuiz(Quizzes quiz) {
        this.quiz = quiz;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
