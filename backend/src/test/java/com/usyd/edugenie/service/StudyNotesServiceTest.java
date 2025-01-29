package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.StudyNotes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.StudyNotesTags;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.repository.StudyNotesRepository;
import com.usyd.edugenie.repository.StudyNotesTagsRepository;
import com.usyd.edugenie.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudyNotesServiceTest {

    @InjectMocks
    private StudyNotesService studyNotesService;

    @Mock
    private StudyNotesRepository studyNotesRepository;

    @Mock
    private StudyNotesTagsRepository studyNotesTagsRepository;

    @Mock
    private TagRepository tagRepository;

    private StudyNotes studyNote;
    private Users user;
    private UUID noteId;
    private UUID tagId;
    private Tag tag;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        noteId = UUID.randomUUID();
        tagId = UUID.randomUUID();
        user = new Users();
        user.setUserId(UUID.randomUUID());

        studyNote = new StudyNotes();
        studyNote.setNoteId(noteId);
        studyNote.setUser(user);

        tag = new Tag(tagId, "Science");
    }

    @Test
    public void testCreateStudyNote() {
        when(studyNotesRepository.save(any(StudyNotes.class))).thenReturn(studyNote);

        StudyNotes createdStudyNote = studyNotesService.createStudyNote(studyNote);

        assertNotNull(createdStudyNote);
        assertEquals(studyNote.getNoteId(), createdStudyNote.getNoteId());
        verify(studyNotesRepository, times(1)).save(studyNote);
    }

    @Test
    public void testGetStudyNoteById() {
        when(studyNotesRepository.findById(noteId)).thenReturn(Optional.of(studyNote));

        Optional<StudyNotes> foundStudyNote = studyNotesService.getStudyNoteById(noteId);

        assertTrue(foundStudyNote.isPresent());
        assertEquals(studyNote.getNoteId(), foundStudyNote.get().getNoteId());
    }

    @Test
    public void testGetAllStudyNotes() {
        List<StudyNotes> notesList = Arrays.asList(studyNote);
        when(studyNotesRepository.findAll()).thenReturn(notesList);

        List<StudyNotes> foundNotes = studyNotesService.getAllStudyNotes();

        assertEquals(1, foundNotes.size());
        verify(studyNotesRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteStudyNote() {
        studyNotesService.deleteStudyNote(noteId);
        verify(studyNotesRepository, times(1)).deleteById(noteId);
    }

    @Test
    public void testGetStudyNotesByUserSortedByDate() {
        List<StudyNotes> userNotes = Arrays.asList(studyNote);
        when(studyNotesRepository.findByUserOrderByGeneratedDateDesc(user)).thenReturn(userNotes);

        List<StudyNotes> notesByUser = studyNotesService.getStudyNotesByUserSortedByDate(user);

        assertEquals(1, notesByUser.size());
        assertEquals(studyNote.getNoteId(), notesByUser.get(0).getNoteId());
    }

    @Test
    public void testGetTagsForStudyNote() {
        List<Tag> tags = Arrays.asList(tag);
        when(studyNotesTagsRepository.findTagsByNoteId(noteId)).thenReturn(tags);

        List<Tag> foundTags = studyNotesService.getTagsForStudyNote(noteId);

        assertEquals(1, foundTags.size());
        assertEquals(tag.getTagId(), foundTags.get(0).getTagId());
    }

    @Test
    public void testAddTagToStudyNote() {
        when(studyNotesRepository.findById(noteId)).thenReturn(Optional.of(studyNote));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        studyNotesService.addTagToStudyNote(noteId, tagId);

        verify(studyNotesTagsRepository, times(1)).save(any(StudyNotesTags.class));
    }

    @Test
    public void testGetTagsForUserNotes() {
        List<StudyNotes> userNotes = Arrays.asList(studyNote);
        when(studyNotesRepository.findByUserOrderByGeneratedDateDesc(user)).thenReturn(userNotes);
        when(studyNotesTagsRepository.findTagsByNoteId(noteId)).thenReturn(Collections.singletonList(tag));

        Set<Tag> userTags = studyNotesService.getTagsForUserNotes(user);

        assertEquals(1, userTags.size());
        assertTrue(userTags.contains(tag));
    }
}
