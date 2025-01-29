package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.StudyNotes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.service.StudyNotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CourseNotesGenControllerTest {

    @InjectMocks
    private CourseNotesGenController courseNotesGenController;

    @Mock
    private StudyNotesService studyNotesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNoteById_notFound() {
        UUID noteId = UUID.randomUUID();

        // Mocking a non-existing note scenario
        when(studyNotesService.getStudyNoteById(noteId)).thenReturn(Optional.empty());

        // Execute
        ResponseEntity<Map<String, Object>> response = courseNotesGenController.getNoteById(noteId);

        // Verify response status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNoteById_found() {
        UUID noteId = UUID.randomUUID();
        StudyNotes mockNote = new StudyNotes();
        mockNote.setNoteId(noteId);
        mockNote.setTitle("Sample Title");
        mockNote.setTopic("Sample Topic");
        mockNote.setContent("Sample Content");
        mockNote.setGeneratedDate(LocalDateTime.now());
        mockNote.setDownloadFile("http://localhost:8088/files/sample.pdf");

        // Mocking a found note scenario
        when(studyNotesService.getStudyNoteById(noteId)).thenReturn(Optional.of(mockNote));

        // Mocking tag retrieval
        List<Tag> mockTags = Arrays.asList(new Tag(UUID.randomUUID(), "Sample Tag"));
        when(studyNotesService.getTagsForStudyNote(noteId)).thenReturn(mockTags);

        // Execute
        ResponseEntity<Map<String, Object>> response = courseNotesGenController.getNoteById(noteId);

        // Verify response status and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sample Title", response.getBody().get("title"));
        assertEquals("Sample Topic", response.getBody().get("topic"));
        assertEquals("Sample Content", response.getBody().get("content"));
        assertEquals("http://localhost:8088/files/sample.pdf", response.getBody().get("downloadFile"));
        assertEquals(Collections.singletonList("Sample Tag"), response.getBody().get("tags"));
    }
}




