package com.usyd.edugenie.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.usyd.edugenie.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {

    @Mock
    private UsersService usersService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    @InjectMocks
    private UserLoginService userLoginService;

    private Users user;
    private Payload mockPayload;

    @BeforeEach
    void setUp() {
        user = new Users("Test", "User", "test@example.com", "http://example.com/picture.jpg",
            LocalDateTime.of(1970, 1, 1, 0, 0), LocalDateTime.now());

        // Initialize Payload mock and configure necessary field behaviors to avoid NullPointerException
        mockPayload = mock(Payload.class, RETURNS_DEEP_STUBS);
        when(mockPayload.getEmail()).thenReturn("test@example.com");

        // Mock getPayloadFromGoogle to return mock payload
        lenient().doReturn(mockPayload).when(userLoginService).getPayloadFromGoogle(anyString());
    }

    @Test
    void testVerifyUser_ExistingUser() {
        when(usersService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateJwtToken(anyString())).thenReturn("mockedToken");

        String token = userLoginService.verifyUser("mocked_google_token");

        assertThat(token).isEqualTo("mockedToken");
        verify(usersService, times(0)).createUser(any(Users.class));
        verify(jwtTokenProvider, times(1)).generateJwtToken("test@example.com");
    }

    @Test
    void testVerifyUser_NewUser() {
        when(usersService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        when(usersService.createUser(any(Users.class))).thenReturn(user);
        when(jwtTokenProvider.generateJwtToken(anyString())).thenReturn("mockedToken");

        String token = userLoginService.verifyUser("mocked_google_token");

        assertThat(token).isEqualTo("mockedToken");
        verify(usersService, times(1)).createUser(any(Users.class));
        verify(jwtTokenProvider, times(1)).generateJwtToken("test@example.com");
    }

    @Test
    void testVerifyUser_InvalidToken() {
        doThrow(new IllegalArgumentException("Invalid ID token.")).when(userLoginService).getPayloadFromGoogle("invalid_google_token");

        assertThrows(IllegalArgumentException.class, () -> userLoginService.verifyUser("invalid_google_token"));
    }
}
