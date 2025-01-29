package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.UserResponses;
import com.usyd.edugenie.repository.UserResponsesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserResponsesServiceTest {

    @Mock
    private UserResponsesRepository userResponsesRepository;

    @InjectMocks
    private UserResponsesService userResponsesService;

    private UserResponses userResponse;
    private UUID responseId;

    @BeforeEach
    void setUp() {
        responseId = UUID.randomUUID();
        userResponse = new UserResponses();
        userResponse.setResponseId(responseId);
        userResponse.setSelectedAnswer("Answer 1");
    }

    @Test
    void testCreateUserResponse() {
        when(userResponsesRepository.save(any(UserResponses.class))).thenReturn(userResponse);

        UserResponses createdResponse = userResponsesService.createUserResponse(userResponse);

        assertThat(createdResponse).isNotNull();
        assertThat(createdResponse.getResponseId()).isEqualTo(responseId);
        verify(userResponsesRepository, times(1)).save(userResponse);
    }

    @Test
    void testGetUserResponseById() {
        when(userResponsesRepository.findById(responseId)).thenReturn(Optional.of(userResponse));

        Optional<UserResponses> foundResponse = userResponsesService.getUserResponseById(responseId);

        assertThat(foundResponse).isPresent();
        assertThat(foundResponse.get().getResponseId()).isEqualTo(responseId);
        verify(userResponsesRepository, times(1)).findById(responseId);
    }

    @Test
    void testGetAllUserResponses() {
        List<UserResponses> userResponsesList = List.of(userResponse);
        when(userResponsesRepository.findAll()).thenReturn(userResponsesList);

        List<UserResponses> allResponses = userResponsesService.getAllUserResponses();

        assertThat(allResponses).hasSize(1);
        assertThat(allResponses.get(0).getResponseId()).isEqualTo(responseId);
        verify(userResponsesRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUserResponse() {
        doNothing().when(userResponsesRepository).deleteById(responseId);

        userResponsesService.deleteUserResponse(responseId);

        verify(userResponsesRepository, times(1)).deleteById(responseId);
    }
}
