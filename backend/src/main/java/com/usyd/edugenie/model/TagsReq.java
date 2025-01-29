package com.usyd.edugenie.model;

import lombok.Data;
import java.util.UUID;


@Data
public class TagsReq {
    private String topic;
    private String studyNotes;
    private UUID quizId;

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStudyNotes() {
        return studyNotes;
    }

    public void setStudyNotes(String studyNotes) {
        this.studyNotes = studyNotes;
    }

    public UUID getQuizId() {
        return quizId;
    }

    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }
}
