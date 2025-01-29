package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@IdClass(StudyNotesTagsId.class)
@Table(name = "study_notes_tags")
public class StudyNotesTags {

    @Id
    @Column(name = "note_id")
    private UUID noteId;

    @Id
    @Column(name = "tag_id")
    private UUID tagId;

    @ManyToOne
    @JoinColumn(name = "note_id", insertable = false, updatable = false)
    private StudyNotes studyNotes;

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;

    // Constructors
    public StudyNotesTags() {}

    public StudyNotesTags(StudyNotes studyNotes, Tag tag) {
        this.noteId = studyNotes.getNoteId();
        this.tagId = tag.getTagId();
        this.studyNotes = studyNotes;
        this.tag = tag;
    }

    // Getters and Setters
    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public StudyNotes getStudyNotes() {
        return studyNotes;
    }

    public void setStudyNotes(StudyNotes studyNotes) {
        this.studyNotes = studyNotes;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
