package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.UserGeneratedStudyNotes;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.repository.UserGeneratedStudyNotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserGeneratedStudyNotesServiceTest {

    @InjectMocks
    private UserGeneratedStudyNotesService userGeneratedStudyNotesService;

    @Mock
    private UserGeneratedStudyNotesRepository userGeneratedStudyNotesRepository;

    private UserGeneratedStudyNotes userNote;
    private UUID userNoteId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Users user = new Users();
        user.setUserId(UUID.randomUUID());

        userNoteId = UUID.randomUUID();
        userNote = new UserGeneratedStudyNotes();
        userNote.setUserNoteId(userNoteId);
        userNote.setUser(user);
        userNote.setContent("Sample content");
        userNote.setUploadDate(LocalDateTime.now());
        userNote.setUploadFile("sample.pdf");
    }

    @Test
    public void testCreateUserGeneratedStudyNotes() {
        when(userGeneratedStudyNotesRepository.save(userNote)).thenReturn(userNote);

        UserGeneratedStudyNotes createdNote = userGeneratedStudyNotesService.createUserGeneratedStudyNotes(userNote);
        assertNotNull(createdNote);
        assertEquals("Sample content", createdNote.getContent());
        verify(userGeneratedStudyNotesRepository, times(1)).save(userNote);
    }

    @Test
    public void testGetUserGeneratedStudyNotesById_Found() {
        when(userGeneratedStudyNotesRepository.findById(userNoteId)).thenReturn(Optional.of(userNote));

        Optional<UserGeneratedStudyNotes> foundNote = userGeneratedStudyNotesService.getUserGeneratedStudyNotesById(userNoteId);
        assertTrue(foundNote.isPresent());
        assertEquals("Sample content", foundNote.get().getContent());
        verify(userGeneratedStudyNotesRepository, times(1)).findById(userNoteId);
    }

    @Test
    public void testGetUserGeneratedStudyNotesById_NotFound() {
        when(userGeneratedStudyNotesRepository.findById(userNoteId)).thenReturn(Optional.empty());

        Optional<UserGeneratedStudyNotes> foundNote = userGeneratedStudyNotesService.getUserGeneratedStudyNotesById(userNoteId);
        assertFalse(foundNote.isPresent());
        verify(userGeneratedStudyNotesRepository, times(1)).findById(userNoteId);
    }

    @Test
    public void testGetAllUserGeneratedStudyNotes() {
        List<UserGeneratedStudyNotes> notesList = Collections.singletonList(userNote);
        when(userGeneratedStudyNotesRepository.findAll()).thenReturn(notesList);

        List<UserGeneratedStudyNotes> allNotes = userGeneratedStudyNotesService.getAllUserGeneratedStudyNotes();
        assertEquals(1, allNotes.size());
        assertEquals("Sample content", allNotes.get(0).getContent());
        verify(userGeneratedStudyNotesRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUserGeneratedStudyNotes() {
        UUID userNoteId = userNote.getUserNoteId();
        userGeneratedStudyNotesService.deleteUserGeneratedStudyNotes(userNoteId);
        verify(userGeneratedStudyNotesRepository, times(1)).deleteById(userNoteId);
    }
}
