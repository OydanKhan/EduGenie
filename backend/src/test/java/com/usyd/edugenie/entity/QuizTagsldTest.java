package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTagsIdTest {

    private QuizTagsId quizTagsId1;
    private QuizTagsId quizTagsId2;
    private UUID quizId;
    private UUID tagId;

    @BeforeEach
    void setUp() {
        quizId = UUID.randomUUID();
        tagId = UUID.randomUUID();

        quizTagsId1 = new QuizTagsId(quizId, tagId);
        quizTagsId2 = new QuizTagsId(quizId, tagId);
    }

    @Test
    void testNoArgsConstructor() {
        QuizTagsId emptyQuizTagsId = new QuizTagsId();
        assertThat(emptyQuizTagsId).isNotNull();
        assertThat(emptyQuizTagsId.getQuizId()).isNull();
        assertThat(emptyQuizTagsId.getTagId()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        assertThat(quizTagsId1.getQuizId()).isEqualTo(quizId);
        assertThat(quizTagsId1.getTagId()).isEqualTo(tagId);
    }

    @Test
    void testGettersAndSetters() {
        QuizTagsId newQuizTagsId = new QuizTagsId();
        newQuizTagsId.setQuizId(quizId);
        newQuizTagsId.setTagId(tagId);

        assertThat(newQuizTagsId.getQuizId()).isEqualTo(quizId);
        assertThat(newQuizTagsId.getTagId()).isEqualTo(tagId);
    }

    @Test
    void testEquals_SameObject() {
        assertThat(quizTagsId1).isEqualTo(quizTagsId1);
    }

    @Test
    void testEquals_DifferentObjectSameValues() {
        assertThat(quizTagsId1).isEqualTo(quizTagsId2);
    }

    @Test
    void testEquals_DifferentValues() {
        QuizTagsId differentQuizTagsId = new QuizTagsId(UUID.randomUUID(), UUID.randomUUID());
        assertThat(quizTagsId1).isNotEqualTo(differentQuizTagsId);
    }

    @Test
    void testHashCode_SameValues() {
        assertThat(quizTagsId1.hashCode()).isEqualTo(quizTagsId2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        QuizTagsId differentQuizTagsId = new QuizTagsId(UUID.randomUUID(), UUID.randomUUID());
        assertThat(quizTagsId1.hashCode()).isNotEqualTo(differentQuizTagsId.hashCode());
    }
}
