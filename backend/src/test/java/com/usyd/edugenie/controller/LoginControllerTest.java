package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.model.UserUpdateReq;
import com.usyd.edugenie.model.UserVerifyReq;
import com.usyd.edugenie.model.LoginUserResp;
import com.usyd.edugenie.service.UserLoginService;
import com.usyd.edugenie.service.UsersService;
import com.usyd.edugenie.service.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserLoginService userLoginService;

    @MockBean
    private UsersService usersService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock // Mock Users instead of creating a real instance
    private Users user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testVerifyGoogleToken() throws Exception {
        UserVerifyReq req = new UserVerifyReq();
        req.setCredential("testCredential");

        String jwtToken = "testJwtToken";

        when(userLoginService.verifyUser("testCredential")).thenReturn(jwtToken);

        mockMvc.perform(post("/user/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(content().string(jwtToken));

        verify(userLoginService, times(1)).verifyUser("testCredential");
    }

    @Test
    public void testGetUserInfo() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Set up user mock responses
        when(user.getEmail()).thenReturn("test@example.com");
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getAvatarUrl()).thenReturn("http://example.com/avatar.jpg");

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/user/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.avatar").value("http://example.com/avatar.jpg"));
    }

    @Test
    public void testUpdateUserInfo() throws Exception {
        UserUpdateReq req = new UserUpdateReq();
        req.setFirstName("JohnUpdated");
        req.setLastName("DoeUpdated");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(content().string("success"));

        verify(usersService, times(1)).updateUser(user);
        verify(user).setFirstName("JohnUpdated");
        verify(user).setLastName("DoeUpdated");
    }
}



