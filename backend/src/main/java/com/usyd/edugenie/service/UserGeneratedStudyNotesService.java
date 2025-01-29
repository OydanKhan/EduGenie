package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.UserGeneratedStudyNotes;
import com.usyd.edugenie.repository.UserGeneratedStudyNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserGeneratedStudyNotesService {

    @Autowired
    private UserGeneratedStudyNotesRepository userGeneratedStudyNotesRepository;

    public UserGeneratedStudyNotes createUserGeneratedStudyNotes(UserGeneratedStudyNotes userNote) {
        return userGeneratedStudyNotesRepository.save(userNote);
    }

    public Optional<UserGeneratedStudyNotes> getUserGeneratedStudyNotesById(UUID userNoteId) {
        return userGeneratedStudyNotesRepository.findById(userNoteId);
    }

    public List<UserGeneratedStudyNotes> getAllUserGeneratedStudyNotes() {
        return userGeneratedStudyNotesRepository.findAll();
    }

    public void deleteUserGeneratedStudyNotes(UUID userNoteId) {
        userGeneratedStudyNotesRepository.deleteById(userNoteId);
    }
}
