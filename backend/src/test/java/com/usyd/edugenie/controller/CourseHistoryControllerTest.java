package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.StudyNotes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.service.StudyNotesService;
import com.usyd.edugenie.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any; // Import for Mockito's any() method

public class CourseHistoryControllerTest {

    @InjectMocks
    private CourseHistoryController courseHistoryController;

    @Mock
    private StudyNotesService studyNotesService;

    @Mock
    private UsersService usersService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up the security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        Users mockUser = new Users();
        mockUser.setUserId(UUID.randomUUID()); // Set a random user ID for the mock user
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    public void testGetCourseHistoryForUser() {
        StudyNotes note = new StudyNotes();
        note.setNoteId(UUID.randomUUID());
        note.setTitle("Sample Course");
        note.setTopic("Java");
        note.setGeneratedDate(LocalDateTime.now());

        // Mock the service to return a list of study notes
        when(studyNotesService.getStudyNotesByUserSortedByDate(any())).thenReturn(Collections.singletonList(note));

        ResponseEntity<List<Map<String, Object>>> response = courseHistoryController.getCourseHistoryForUser(null, null, null, null);

        // Check if the response is successful
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Additional assertions can be made here to validate the response content
    }

    @Test
    public void testGetTagsForCurrentUser() {
        Tag tag = new Tag(UUID.randomUUID(), "Data Science");
        tag.setTagId(UUID.randomUUID());
        tag.setName("Java");

        // Mock the service to return a set of tags
        when(studyNotesService.getTagsForUserNotes(any())).thenReturn(Collections.singleton(tag));

        ResponseEntity<List<Tag>> response = courseHistoryController.getTagsForCurrentUser();

        // Check if the response is successful
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Additional assertions can be made here to validate the response content
    }
}
