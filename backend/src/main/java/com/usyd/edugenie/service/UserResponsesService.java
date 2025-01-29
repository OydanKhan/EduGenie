package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.UserResponses;
import com.usyd.edugenie.repository.UserResponsesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserResponsesService {

    @Autowired
    private UserResponsesRepository userResponsesRepository;

    public UserResponses createUserResponse(UserResponses userResponse) {
        return userResponsesRepository.save(userResponse);
    }

    public Optional<UserResponses> getUserResponseById(UUID responseId) {
        return userResponsesRepository.findById(responseId);
    }

    public List<UserResponses> getAllUserResponses() {
        return userResponsesRepository.findAll();
    }

    public void deleteUserResponse(UUID responseId) {
        userResponsesRepository.deleteById(responseId);
    }
}
