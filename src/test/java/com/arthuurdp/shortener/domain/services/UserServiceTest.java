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

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "User";
    private static final String LAST_NAME = "Test";
    private static final String EMAIL = "user@test.com";

    private User user;
    private UserWithUrlsDTO userWithUrlsDTO;

    @BeforeEach
    void setUp() {
        user = new User(
                USER_ID,
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                "password",
                Role.ROLE_USER
        );

        userWithUrlsDTO = new UserWithUrlsDTO(
                USER_ID,
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                Role.ROLE_USER,
                null
        );
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findByIdWithShortUrls(USER_ID))
                .thenReturn(Optional.of(user));
        when(entityMapper.toUserWithUrlsDTO(user))
                .thenReturn(userWithUrlsDTO);

        UserWithUrlsDTO result = userService.findById(USER_ID);

        assertNotNull(result);
        assertEquals(EMAIL, result.email());

        verify(userRepository).findByIdWithShortUrls(USER_ID);
        verify(entityMapper).toUserWithUrlsDTO(user);
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findByIdWithShortUrls(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.findById(USER_ID));

        verify(userRepository).findByIdWithShortUrls(USER_ID);
    }

    @Test
    void testUpdateUser_Success() {
        UpdateUserDTO updateDTO = new UpdateUserDTO(
                "Napoleão",
                "Bonaparte",
                "napoleao@test.com",
                null,
                Role.ROLE_ADMIN
        );

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(entityMapper.toUserWithUrlsDTO(user))
                .thenReturn(userWithUrlsDTO);

        UserWithoutUrlsDTO result = userService.updateUser(USER_ID, updateDTO);

        assertNotNull(result);

        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(any(User.class));
        verify(entityMapper).toUserWithUrlsDTO(user);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        userService.deleteUser(USER_ID);

        verify(userRepository).findById(USER_ID);
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(USER_ID));

        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).delete(any());
    }
}