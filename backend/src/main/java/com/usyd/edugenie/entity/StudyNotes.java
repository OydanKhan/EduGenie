package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "study_notes")
public class StudyNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID noteId;

    // maps the whole User object to StudyNotes
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private Users user;


    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String topic;

    @Column(length = 255)
    private String downloadFile;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime generatedDate;


    // Default constructor
    public StudyNotes() {}

    // Parameterized constructor
    public StudyNotes(Users user, String title, String content, LocalDateTime generatedDate, String topic, String downloadFile) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.generatedDate = generatedDate;
        this.topic = topic;
        this.downloadFile = downloadFile;
    }

    // Getters and setters
    public UUID getNoteId() {
        return noteId;
    }

    public void setNoteId(UUID noteId) {
        this.noteId = noteId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(String downloadFile) {
        this.downloadFile = downloadFile;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyNotes that = (StudyNotes) o;
        return Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId);
    }

    // toString()
    @Override
    public String toString() {
        return "StudyNotes{" +
            "noteId=" + noteId +
            ", title='" + title + '\'' +
            ", topic='" + topic + '\'' +
            '}';
    }
}
