package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagScoreTest {

    @Test
    void testNoArgsConstructor() {
        TagScore tagScore = new TagScore();
        assertThat(tagScore).isNotNull();
        assertThat(tagScore.getTag()).isNull();
        assertThat(tagScore.getAvgScore()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        TagScore tagScore = new TagScore("Math", 85);
        assertThat(tagScore.getTag()).isEqualTo("Math");
        assertThat(tagScore.getAvgScore()).isEqualTo(85);
    }

    @Test
    void testSettersAndGetters() {
        TagScore tagScore = new TagScore();
        tagScore.setTag("Science");
        tagScore.setAvgScore(90);

        assertThat(tagScore.getTag()).isEqualTo("Science");
        assertThat(tagScore.getAvgScore()).isEqualTo(90);
    }

    @Test
    void testEqualsAndHashCode() {
        TagScore tagScore1 = new TagScore("English", 75);
        TagScore tagScore2 = new TagScore("English", 75);
        assertThat(tagScore1).isEqualTo(tagScore2);
        assertThat(tagScore1.hashCode()).isEqualTo(tagScore2.hashCode());
    }

    @Test
    void testToString() {
        TagScore tagScore = new TagScore("History", 80);
        String expectedString = "TagScore(tag=History, avgScore=80)";
        assertThat(tagScore.toString()).isEqualTo(expectedString);
    }
}
