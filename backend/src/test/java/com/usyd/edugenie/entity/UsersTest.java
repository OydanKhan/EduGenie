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

class UsersTest {

    private Users user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialize Validator for testing validation constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Initialize Users object with valid values
        user = new Users();
        user.setUserId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setAvatarUrl("http://example.com/avatar.jpg");
        user.setDateOfBirth(LocalDateTime.of(1990, 1, 1, 0, 0));
        user.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void testUserConstructor() {
        LocalDateTime dob = LocalDateTime.of(1990, 1, 1, 0, 0);
        LocalDateTime registration = LocalDateTime.now();

        Users newUser = new Users("Jane", "Smith", "jane.smith@example.com", "http://example.com/avatar2.jpg", dob, registration);

        assertEquals("Jane", newUser.getFirstName());
        assertEquals("Smith", newUser.getLastName());
        assertEquals("jane.smith@example.com", newUser.getEmail());
        assertEquals("http://example.com/avatar2.jpg", newUser.getAvatarUrl());
        assertEquals(dob, newUser.getDateOfBirth());
        assertEquals(registration, newUser.getRegistrationDate());
    }

    @Test
    void testGettersAndSetters() {
        UUID userId = UUID.randomUUID();
        user.setUserId(userId);
        user.setFirstName("Alice");
        user.setLastName("Johnson");
        user.setEmail("alice.johnson@example.com");
        user.setAvatarUrl("http://example.com/avatar3.jpg");

        assertEquals(userId, user.getUserId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Johnson", user.getLastName());
        assertEquals("alice.johnson@example.com", user.getEmail());
        assertEquals("http://example.com/avatar3.jpg", user.getAvatarUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID userId = UUID.randomUUID();
        user.setUserId(userId);

        Users anotherUser = new Users();
        anotherUser.setUserId(userId);

        assertEquals(user, anotherUser);
        assertEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void testValidationConstraints() {
        // Setting fields to invalid values to trigger validation errors
        user.setFirstName(null);
        user.setLastName(null);
        user.setEmail("invalid-email");  // Invalid email format
        user.setAvatarUrl(null);  // Null value to check avatarUrl validation

        // Validate the user and log constraint violations
        Set<ConstraintViolation<Users>> violations = validator.validate(user);
        violations.forEach(v -> System.out.println("Violation on: " + v.getPropertyPath() + " - " + v.getMessage()));

        // Adjust the expected constraint violation count to 4
        assertEquals(4, violations.size(), "Expected exactly 4 constraint violations");

        // Check specific violations for fields
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("avatarUrl")));
    }

    @Test
    void testToString() {
        String toStringResult = user.toString();
        assertTrue(toStringResult.contains("userId"));
        assertTrue(toStringResult.contains("firstName='John'"));
        assertTrue(toStringResult.contains("lastName='Doe'"));
        assertTrue(toStringResult.contains("email='john.doe@example.com'"));
    }
}
