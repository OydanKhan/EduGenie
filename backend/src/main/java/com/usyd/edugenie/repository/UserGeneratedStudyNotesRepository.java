package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.UserGeneratedStudyNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserGeneratedStudyNotesRepository extends JpaRepository<UserGeneratedStudyNotes, UUID> {

}
