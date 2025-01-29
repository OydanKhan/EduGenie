package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TagsReqTest {

    @Test
    void testNoArgsConstructor() {
        TagsReq tagsReq = new TagsReq();
        assertThat(tagsReq).isNotNull();
        assertThat(tagsReq.getTopic()).isNull();
        assertThat(tagsReq.getStudyNotes()).isNull();
        assertThat(tagsReq.getQuizId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        TagsReq tagsReq = new TagsReq();
        UUID quizId = UUID.randomUUID();

        tagsReq.setTopic("Science");
        tagsReq.setStudyNotes("Study notes content");
        tagsReq.setQuizId(quizId);

        assertThat(tagsReq.getTopic()).isEqualTo("Science");
        assertThat(tagsReq.getStudyNotes()).isEqualTo("Study notes content");
        assertThat(tagsReq.getQuizId()).isEqualTo(quizId);
    }

    @Test
    void testToString() {
        UUID quizId = UUID.randomUUID();
        TagsReq tagsReq = new TagsReq();
        tagsReq.setTopic("Math");
        tagsReq.setStudyNotes("Some study notes");
        tagsReq.setQuizId(quizId);

        String expectedString = "TagsReq(topic=Math, studyNotes=Some study notes, quizId=" + quizId + ")";
        assertThat(tagsReq.toString()).isEqualTo(expectedString);
    }
}
