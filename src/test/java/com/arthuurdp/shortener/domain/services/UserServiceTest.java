package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.user.*;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapperService entityMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserWithUrlsDTO userWithUrlsDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "User", "Test", "user@teste.com", "teste123", Role.ROLE_USER);
        userWithUrlsDTO = new UserWithUrlsDTO(1L, "User", "teste", "user@teste.com", Role.ROLE_USER, null);
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(userRepository.findByIdWithShortUrls(1L)).thenReturn(Optional.of(user));
        when(entityMapper.toUserWithUrlsDTO(user)).thenReturn(userWithUrlsDTO);

        // Act
        UserWithUrlsDTO result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("user@teste.com", result.email());
        verify(userRepository, times(1)).findByIdWithShortUrls(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(userRepository.findByIdWithShortUrls(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindAll_WithUrls_Success() {
        // Arrange
        when(userRepository.findAllWithShortUrls()).thenReturn(Arrays.asList(user));
        when(entityMapper.toUserWithUrlsDTO(any())).thenReturn(userWithUrlsDTO);

        // Act
        List<UserWithUrlsDTO> result = userService.findAllWithUrls();

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllWithShortUrls();
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UpdateUserDTO updateDTO = new UpdateUserDTO("napoleão", "bonaparte", "napoleao@teste.com", null, Role.ROLE_ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityMapper.toUserWithUrlsDTO(any())).thenReturn(userWithUrlsDTO);

        // Act
        UserWithUrlsDTO result = userService.updateUser(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }


    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));

        verify(userRepository, never()).delete(any());
    }
}