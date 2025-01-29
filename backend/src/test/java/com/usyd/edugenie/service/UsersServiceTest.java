package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private Users testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        testUser = new Users();
        testUser.setUserId(userId);
        testUser.setEmail("test@example.com");
    }

    @Test
    void testCreateUser() {
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        Users createdUser = usersService.createUser(testUser);

        assertNotNull(createdUser);
        assertEquals(testUser.getUserId(), createdUser.getUserId());
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateUser() {
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        Users updatedUser = usersService.updateUser(testUser);

        assertNotNull(updatedUser);
        assertEquals(testUser.getUserId(), updatedUser.getUserId());
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    void testGetUserById() {
        when(usersRepository.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<Users> foundUser = usersService.getUserById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getUserId(), foundUser.get().getUserId());
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAllUsers() {
        when(usersRepository.findAll()).thenReturn(List.of(testUser));

        List<Users> usersList = usersService.getAllUsers();

        assertNotNull(usersList);
        assertEquals(1, usersList.size());
        assertEquals(testUser.getUserId(), usersList.get(0).getUserId());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser() {
        doNothing().when(usersRepository).deleteById(userId);

        usersService.deleteUser(userId);

        verify(usersRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetUserByEmail() {
        when(usersRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        Optional<Users> foundUser = usersService.getUserByEmail(testUser.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        verify(usersRepository, times(1)).findByEmail(testUser.getEmail());
    }
}
