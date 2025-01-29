package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.StudyNotesTags;
import com.usyd.edugenie.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudyNotesTagsRepository extends JpaRepository<StudyNotesTags, UUID> {

    @Query("SELECT st.tag FROM StudyNotesTags st WHERE st.id.noteId = :noteId")
    List<Tag> findTagsByNoteId(@Param("noteId") UUID noteId);
}
