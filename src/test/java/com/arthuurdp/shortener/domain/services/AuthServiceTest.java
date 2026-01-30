package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.user.*;
import com.arthuurdp.shortener.domain.repositories.UserRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import com.arthuurdp.shortener.infrastructure.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapperService entityMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User user;
    private AuthDTO authDTO;
    private RegisterUserDTO registerDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "User", "Test", "user@teste.com", "teste123", Role.ROLE_USER);
        authDTO = new AuthDTO("user@teste.com", "teste123");
        registerDTO = new RegisterUserDTO("User", "Test", "user@teste.com", "teste123", Role.ROLE_USER);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(authManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("token-123");

        // Act
        LoginResponseDTO result = authService.login(authDTO);

        // Assert
        assertNotNull(result);
        assertEquals("token-123", result.token());
        verify(authManager, times(1)).authenticate(any());
    }

    @Test
    void testRegister_Success() {
        // Arrange
        UserResponseDTO response = new UserResponseDTO(1L, "User", "Test", "user@teste.com", Role.ROLE_USER, null);
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("teste123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityMapper.toUserDTO(any(User.class))).thenReturn(response);

        // Act
        UserResponseDTO result = authService.register(registerDTO);

        // Assert
        assertNotNull(result);
        assertEquals("user@teste.com", result.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(registerDTO.email())).thenReturn(user);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authService.register(registerDTO));
        verify(userRepository, never()).save(any());
    }
}