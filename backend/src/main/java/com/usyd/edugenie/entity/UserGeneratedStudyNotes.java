package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "user_generated_study_notes")
public class UserGeneratedStudyNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userNoteId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @NotNull // Add @NotNull to ensure validation for the content field
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime uploadDate;

    @Column(length = 255)
    private String uploadFile;

    // Default constructor
    public UserGeneratedStudyNotes() {}

    // Parameterized constructor
    public UserGeneratedStudyNotes(Users user, String content, LocalDateTime uploadDate, String uploadFile) {
        this.user = user;
        this.content = content;
        this.uploadDate = uploadDate;
        this.uploadFile = uploadFile;
    }

    // Getters and setters
    public UUID getUserNoteId() {
        return userNoteId;
    }

    public void setUserNoteId(UUID userNoteId) {
        this.userNoteId = userNoteId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGeneratedStudyNotes that = (UserGeneratedStudyNotes) o;
        return Objects.equals(userNoteId, that.userNoteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNoteId);
    }

    // toString()
    @Override
    public String toString() {
        return "UserGeneratedStudyNotes{" +
            "userNoteId=" + userNoteId +
            ", content='" + content + '\'' +
            '}';
    }
}
