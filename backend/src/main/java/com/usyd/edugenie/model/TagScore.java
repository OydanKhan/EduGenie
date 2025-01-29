package com.usyd.edugenie.model;

import lombok.Data;

/**
 * @author caorong
 */
@Data
public class TagScore {
    private String tag;
    private Integer avgScore;

    // No-argument constructor
    public TagScore() {}

    // Parameterized constructor
    public TagScore(String tag, Integer avgScore) {
        this.tag = tag;
        this.avgScore = avgScore;
    }
}
