package com.usyd.edugenie.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class StudyNotesTagsId implements Serializable {
    private UUID noteId;
    private UUID tagId;

    // Default constructor
    public StudyNotesTagsId() {}

    public StudyNotesTagsId(UUID noteId, UUID tagId) {
        this.noteId = noteId;
        this.tagId = tagId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyNotesTagsId)) return false;
        StudyNotesTagsId that = (StudyNotesTagsId) o;
        return Objects.equals(noteId, that.noteId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, tagId);
    }
}
