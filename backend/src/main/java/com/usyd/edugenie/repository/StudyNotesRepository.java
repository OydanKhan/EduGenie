package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.StudyNotes;
import com.usyd.edugenie.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface StudyNotesRepository extends JpaRepository<StudyNotes, UUID> {
    List<StudyNotes> findByUserOrderByGeneratedDateDesc(Users user);
}
