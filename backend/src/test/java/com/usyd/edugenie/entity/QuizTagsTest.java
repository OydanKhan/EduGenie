package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTagsTest {

    private QuizTags quizTags;
    private Quizzes quiz;
    private Tag tag;
    private UUID quizId;
    private UUID tagId;

    @BeforeEach
    void setUp() {
        quizId = UUID.randomUUID();
        tagId = UUID.randomUUID();
        quiz = new Quizzes();
        quiz.setQuizId(quizId);
        tag = new Tag();
        tag.setTagId(tagId);

        quizTags = new QuizTags(quiz, tag);
    }

    @Test
    void testNoArgsConstructor() {
        QuizTags emptyQuizTags = new QuizTags();
        assertThat(emptyQuizTags).isNotNull();
        assertThat(emptyQuizTags.getQuiz()).isNull();
        assertThat(emptyQuizTags.getTag()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        assertThat(quizTags.getQuiz()).isEqualTo(quiz);
        assertThat(quizTags.getTag()).isEqualTo(tag);
        assertThat(quizTags.getQuizId()).isEqualTo(quizId);
        assertThat(quizTags.getTagId()).isEqualTo(tagId);
    }

    @Test
    void testGettersAndSetters() {
        QuizTags newQuizTags = new QuizTags();
        newQuizTags.setQuizId(quizId);
        newQuizTags.setTagId(tagId);
        newQuizTags.setQuiz(quiz);
        newQuizTags.setTag(tag);

        assertThat(newQuizTags.getQuizId()).isEqualTo(quizId);
        assertThat(newQuizTags.getTagId()).isEqualTo(tagId);
        assertThat(newQuizTags.getQuiz()).isEqualTo(quiz);
        assertThat(newQuizTags.getTag()).isEqualTo(tag);
    }
}
