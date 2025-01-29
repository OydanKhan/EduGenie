package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudyNotesTagsIdTest {

    private StudyNotesTagsId studyNotesTagsId;
    private UUID noteId;
    private UUID tagId;

    @BeforeEach
    void setUp() {
        noteId = UUID.randomUUID();
        tagId = UUID.randomUUID();
        studyNotesTagsId = new StudyNotesTagsId(noteId, tagId);
    }

    @Test
    void testNoArgsConstructor() {
        StudyNotesTagsId emptyId = new StudyNotesTagsId();
        assertThat(emptyId).isNotNull();
        assertThat(emptyId.getNoteId()).isNull();
        assertThat(emptyId.getTagId()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        assertThat(studyNotesTagsId.getNoteId()).isEqualTo(noteId);
        assertThat(studyNotesTagsId.getTagId()).isEqualTo(tagId);
    }

    @Test
    void testGettersAndSetters() {
        StudyNotesTagsId newId = new StudyNotesTagsId();
        newId.setNoteId(noteId);
        newId.setTagId(tagId);

        assertThat(newId.getNoteId()).isEqualTo(noteId);
        assertThat(newId.getTagId()).isEqualTo(tagId);
    }

    @Test
    void testEquals() {
        StudyNotesTagsId sameId = new StudyNotesTagsId(noteId, tagId);
        StudyNotesTagsId differentId = new StudyNotesTagsId(UUID.randomUUID(), UUID.randomUUID());

        assertThat(studyNotesTagsId).isEqualTo(sameId);
        assertThat(studyNotesTagsId).isNotEqualTo(differentId);
    }

    @Test
    void testHashCode() {
        StudyNotesTagsId sameId = new StudyNotesTagsId(noteId, tagId);
        assertThat(studyNotesTagsId.hashCode()).isEqualTo(sameId.hashCode());
    }
}
