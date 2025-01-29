package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginUserRespTest {

    @Test
    void testLoginUserRespGettersAndSetters() {
        // Arrange
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String avatar = "http://example.com/avatar.jpg";

        // Act
        LoginUserResp loginUserResp = new LoginUserResp();
        loginUserResp.setEmail(email);
        loginUserResp.setLastName(lastName);
        loginUserResp.setFirstName(firstName);
        loginUserResp.setAvatar(avatar);

        // Assert
        assertThat(loginUserResp.getEmail()).isEqualTo(email);
        assertThat(loginUserResp.getLastName()).isEqualTo(lastName);
        assertThat(loginUserResp.getFirstName()).isEqualTo(firstName);
        assertThat(loginUserResp.getAvatar()).isEqualTo(avatar);
    }

    @Test
    void testToStringMethod() {
        // Arrange
        LoginUserResp loginUserResp = new LoginUserResp();
        loginUserResp.setEmail("test@example.com");
        loginUserResp.setLastName("Doe");
        loginUserResp.setFirstName("John");
        loginUserResp.setAvatar("http://example.com/avatar.jpg");

        // Act & Assert
        assertThat(loginUserResp.toString()).contains("test@example.com", "Doe", "John", "http://example.com/avatar.jpg");
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        LoginUserResp user1 = new LoginUserResp();
        user1.setEmail("test@example.com");
        user1.setLastName("Doe");
        user1.setFirstName("John");
        user1.setAvatar("http://example.com/avatar.jpg");

        LoginUserResp user2 = new LoginUserResp();
        user2.setEmail("test@example.com");
        user2.setLastName("Doe");
        user2.setFirstName("John");
        user2.setAvatar("http://example.com/avatar.jpg");

        // Act & Assert
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
