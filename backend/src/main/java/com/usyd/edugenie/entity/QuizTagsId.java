package com.usyd.edugenie.entity;

import java.io.Serializable;
import java.util.*;

public class QuizTagsId implements Serializable {

    private UUID quizId;
    private UUID tagId;

    // Default constructor
    public QuizTagsId() {}

    // Parameterized constructor
    public QuizTagsId(UUID quizId, UUID tagId) {
        this.quizId = quizId;
        this.tagId = tagId;
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

    // Override equals and hashCode for composite key comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizTagsId that = (QuizTagsId) o;
        return quizId.equals(that.quizId) && tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, tagId);
    }
}
