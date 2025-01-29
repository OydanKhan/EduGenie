package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudyNotesTest {

    private StudyNotes studyNotes;
    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(UUID.randomUUID());

        studyNotes = new StudyNotes();
        studyNotes.setNoteId(UUID.randomUUID());
        studyNotes.setUser(user);
        studyNotes.setTitle("Sample Title");
        studyNotes.setContent("Sample content for study notes.");
        studyNotes.setGeneratedDate(LocalDateTime.now());
        studyNotes.setTopic("Sample Topic");
        studyNotes.setDownloadFile("sample_file.pdf");
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime generatedDate = LocalDateTime.now();
        StudyNotes paramStudyNotes = new StudyNotes(user, "Parameterized Title", "Content for param study notes.", generatedDate, "Param Topic", "param_file.pdf");

        assertEquals(user, paramStudyNotes.getUser());
        assertEquals("Parameterized Title", paramStudyNotes.getTitle());
        assertEquals("Content for param study notes.", paramStudyNotes.getContent());
        assertEquals(generatedDate, paramStudyNotes.getGeneratedDate());
        assertEquals("Param Topic", paramStudyNotes.getTopic());
        assertEquals("param_file.pdf", paramStudyNotes.getDownloadFile());
    }

    @Test
    void testGettersAndSetters() {
        UUID noteId = UUID.randomUUID();
        studyNotes.setNoteId(noteId);
        studyNotes.setTitle("Updated Title");
        studyNotes.setContent("Updated content.");
        LocalDateTime generatedDate = LocalDateTime.now();
        studyNotes.setGeneratedDate(generatedDate);
        studyNotes.setTopic("Updated Topic");
        studyNotes.setDownloadFile("updated_file.pdf");

        assertEquals(noteId, studyNotes.getNoteId());
        assertEquals("Updated Title", studyNotes.getTitle());
        assertEquals("Updated content.", studyNotes.getContent());
        assertEquals(generatedDate, studyNotes.getGeneratedDate());
        assertEquals("Updated Topic", studyNotes.getTopic());
        assertEquals("updated_file.pdf", studyNotes.getDownloadFile());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID noteId = UUID.randomUUID();
        studyNotes.setNoteId(noteId);

        StudyNotes anotherStudyNotes = new StudyNotes();
        anotherStudyNotes.setNoteId(noteId);

        assertEquals(studyNotes, anotherStudyNotes);
        assertEquals(studyNotes.hashCode(), anotherStudyNotes.hashCode());
    }

    @Test
    void testNotEquals() {
        StudyNotes differentStudyNotes = new StudyNotes();
        differentStudyNotes.setNoteId(UUID.randomUUID());

        assertNotEquals(studyNotes, differentStudyNotes);
        assertNotEquals(studyNotes.hashCode(), differentStudyNotes.hashCode());
    }

    @Test
    void testToString() {
        String toStringResult = studyNotes.toString();
        assertTrue(toStringResult.contains("noteId="));
        assertTrue(toStringResult.contains("title='Sample Title'"));
        assertTrue(toStringResult.contains("topic='Sample Topic'"));
    }
}
