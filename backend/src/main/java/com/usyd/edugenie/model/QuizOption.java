package com.usyd.edugenie.model;

public class QuizOption {
    private String option;
    private boolean correct;

    // Default constructor
    public QuizOption() {
    }

    // Parameterized constructor
    public QuizOption(String option, boolean correct) {
        this.option = option;
        this.correct = correct;
    }

    // Getter and Setter for option
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    // Getter and Setter for correct
    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}