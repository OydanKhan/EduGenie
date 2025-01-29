package com.usyd.edugenie.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.service.JwtTokenProvider;
import com.usyd.edugenie.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UsersService usersService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private static final String VALID_TOKEN = "valid-token";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_withValidToken() throws ServletException, IOException {
        // Mock a valid JWT and user
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        Users user = new Users();
        user.setUserId(UUID.randomUUID());

        // Mock behavior
        request.setRequestURI("/api/resource");
        request.addHeader("authorization", "Bearer " + VALID_TOKEN);
        when(jwtTokenProvider.verifyJwtToken(VALID_TOKEN)).thenReturn(decodedJWT);
        when(jwtTokenProvider.getUserEmailFromDecoded(decodedJWT)).thenReturn("test@example.com");
        when(usersService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Execute the filter
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Assertions
        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_withInvalidToken() throws ServletException, IOException {
        request.setRequestURI("/api/resource");
        request.addHeader("authorization", "Bearer invalid-token");

        when(jwtTokenProvider.verifyJwtToken("invalid-token")).thenThrow(new RuntimeException("Invalid token"));

        // Execute the filter
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Assertions
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_withoutAuthorizationHeader() throws ServletException, IOException {
        request.setRequestURI("/api/resource");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_loginPath() throws ServletException, IOException {
        request.setRequestURI("/login/verify");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}

