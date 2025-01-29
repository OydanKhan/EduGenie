package com.usyd.edugenie.model;

import java.util.List;

public class QuizQuestion {
    private String text;
    private List<QuizOption> options;

    // Default constructor
    public QuizQuestion() {
    }

    // Parameterized constructor
    public QuizQuestion(String text, List<QuizOption> options) {
        this.text = text;
        this.options = options;
    }

    // Getter and Setter for text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Getter and Setter for options
    public List<QuizOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuizOption> options) {
        this.options = options;
    }
}
