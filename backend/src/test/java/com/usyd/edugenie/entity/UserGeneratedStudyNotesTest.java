package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserGeneratedStudyNotesTest {

    private UserGeneratedStudyNotes userGeneratedStudyNotes;
    private Users testUser;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialize validator for testing validation constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Create a test user and initialize UserGeneratedStudyNotes with valid data
        testUser = new Users();
        testUser.setUserId(UUID.randomUUID());

        userGeneratedStudyNotes = new UserGeneratedStudyNotes();
        userGeneratedStudyNotes.setUserNoteId(UUID.randomUUID());
        userGeneratedStudyNotes.setUser(testUser);
        userGeneratedStudyNotes.setContent("Sample content for study notes.");
        userGeneratedStudyNotes.setUploadDate(LocalDateTime.now());
        userGeneratedStudyNotes.setUploadFile("sampleFile.txt");
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime uploadDate = LocalDateTime.now();
        UserGeneratedStudyNotes notes = new UserGeneratedStudyNotes(testUser, "Another content sample", uploadDate, "anotherFile.txt");

        assertEquals(testUser, notes.getUser());
        assertEquals("Another content sample", notes.getContent());
        assertEquals(uploadDate, notes.getUploadDate());
        assertEquals("anotherFile.txt", notes.getUploadFile());
    }

    @Test
    void testGettersAndSetters() {
        UUID userNoteId = UUID.randomUUID();
        LocalDateTime uploadDate = LocalDateTime.now().minusDays(1);

        userGeneratedStudyNotes.setUserNoteId(userNoteId);
        userGeneratedStudyNotes.setContent("Updated content");
        userGeneratedStudyNotes.setUploadDate(uploadDate);
        userGeneratedStudyNotes.setUploadFile("updatedFile.txt");

        assertEquals(userNoteId, userGeneratedStudyNotes.getUserNoteId());
        assertEquals("Updated content", userGeneratedStudyNotes.getContent());
        assertEquals(uploadDate, userGeneratedStudyNotes.getUploadDate());
        assertEquals("updatedFile.txt", userGeneratedStudyNotes.getUploadFile());
    }

    @Test
    void testEqualsAndHashCode() {
        UserGeneratedStudyNotes anotherNotes = new UserGeneratedStudyNotes();
        anotherNotes.setUserNoteId(userGeneratedStudyNotes.getUserNoteId());

        assertEquals(userGeneratedStudyNotes, anotherNotes);
        assertEquals(userGeneratedStudyNotes.hashCode(), anotherNotes.hashCode());
    }

    @Test
    void testValidationConstraints() {
        // Set content to null to test the validation constraint
        userGeneratedStudyNotes.setContent(null);

        Set<ConstraintViolation<UserGeneratedStudyNotes>> violations = validator.validate(userGeneratedStudyNotes);
        violations.forEach(v -> System.out.println("Violation on: " + v.getPropertyPath() + " - " + v.getMessage()));

        // Adjust the expected constraint violation count based on validations in the entity
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("content")));
    }

    @Test
    void testToString() {
        String toStringResult = userGeneratedStudyNotes.toString();
        assertTrue(toStringResult.contains("userNoteId"));
        assertTrue(toStringResult.contains("content='Sample content for study notes.'"));
    }
}

