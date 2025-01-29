package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudyNotesTagsTest {

    private StudyNotesTags studyNotesTags;
    private UUID noteId;
    private UUID tagId;
    private StudyNotes studyNotes;
    private Tag tag;

    @BeforeEach
    void setUp() {
        noteId = UUID.randomUUID();
        tagId = UUID.randomUUID();

        // Creating mock StudyNotes and Tag entities
        studyNotes = new StudyNotes();
        studyNotes.setNoteId(noteId);

        tag = new Tag();
        tag.setTagId(tagId);

        studyNotesTags = new StudyNotesTags(studyNotes, tag);
    }

    @Test
    void testNoArgsConstructor() {
        StudyNotesTags emptyStudyNotesTags = new StudyNotesTags();
        assertThat(emptyStudyNotesTags).isNotNull();
        assertThat(emptyStudyNotesTags.getNoteId()).isNull();
        assertThat(emptyStudyNotesTags.getTagId()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        assertThat(studyNotesTags.getNoteId()).isEqualTo(noteId);
        assertThat(studyNotesTags.getTagId()).isEqualTo(tagId);
        assertThat(studyNotesTags.getStudyNotes()).isEqualTo(studyNotes);
        assertThat(studyNotesTags.getTag()).isEqualTo(tag);
    }

    @Test
    void testGettersAndSetters() {
        StudyNotesTags newStudyNotesTags = new StudyNotesTags();

        newStudyNotesTags.setNoteId(noteId);
        newStudyNotesTags.setTagId(tagId);
        newStudyNotesTags.setStudyNotes(studyNotes);
        newStudyNotesTags.setTag(tag);

        assertThat(newStudyNotesTags.getNoteId()).isEqualTo(noteId);
        assertThat(newStudyNotesTags.getTagId()).isEqualTo(tagId);
        assertThat(newStudyNotesTags.getStudyNotes()).isEqualTo(studyNotes);
        assertThat(newStudyNotesTags.getTag()).isEqualTo(tag);
    }
}
