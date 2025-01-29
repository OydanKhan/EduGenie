package com.usyd.edugenie.model;

import lombok.Data;

@Data
public class QuizGenerateReq {
    private String topic;
    private String studyNotes;
    private int numberOfQuestions;

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

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
