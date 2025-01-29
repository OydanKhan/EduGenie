package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.StudyNotes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.StudyNotesTags;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.repository.StudyNotesRepository;
import com.usyd.edugenie.repository.StudyNotesTagsRepository;
import com.usyd.edugenie.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudyNotesService {

    @Autowired
    private StudyNotesRepository studyNotesRepository;

    @Autowired
    private StudyNotesTagsRepository studyNotesTagsRepository;

    @Autowired
    private TagRepository tagRepository;

    public StudyNotes createStudyNote(StudyNotes studyNote) {
        return studyNotesRepository.save(studyNote);
    }

    public Optional<StudyNotes> getStudyNoteById(UUID noteId) {
        return studyNotesRepository.findById(noteId);
    }

    public List<StudyNotes> getAllStudyNotes() {
        return studyNotesRepository.findAll();
    }

    public void deleteStudyNote(UUID noteId) {
        studyNotesRepository.deleteById(noteId);
    }

    public List<StudyNotes>  getStudyNotesByUserSortedByDate(Users user) { return studyNotesRepository.findByUserOrderByGeneratedDateDesc(user); }

    // Fetch tags associated with a study note
    public List<Tag> getTagsForStudyNote(UUID noteId) {
        return studyNotesTagsRepository.findTagsByNoteId(noteId);
    }

    // Add a tag to a study note
    public void addTagToStudyNote(UUID noteId, UUID tagId) {
        // Find the study note and tag
        StudyNotes studyNote = studyNotesRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Study note not found"));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));

        StudyNotesTags studyNotesTag = new StudyNotesTags(studyNote, tag);
        studyNotesTagsRepository.save(studyNotesTag);
    }
    public Set<Tag> getTagsForUserNotes(Users user) {
        List<StudyNotes> userNotes = getStudyNotesByUserSortedByDate(user);
        Set<Tag> tags = new HashSet<>();

        for (StudyNotes note : userNotes) {
            List<Tag> noteTags = studyNotesTagsRepository.findTagsByNoteId(note.getNoteId());
            tags.addAll(noteTags);
        }

        return tags;
    }

}
