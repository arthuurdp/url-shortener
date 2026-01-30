package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.*;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.AccessDeniedException;
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
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private EntityMapperService entityMapper;

    @Mock
    private ShortKeyGeneratorService keyGenerator;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ShortUrlService shortUrlService;

    private User user;
    private ShortUrl shortUrl;
    private CreateShortUrlDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "User", "Test", "user@teste.com", "teste123", Role.ROLE_USER);
        shortUrl = new ShortUrl("abc123", "https://example.com", user);
        createDTO = new CreateShortUrlDTO("https://example.com");
    }

    @Test
    void testGetAll_UserRole() {
        // Arrange
        ShortUrlDTO dto = new ShortUrlDTO(1L, "abc123", "https://example.com", null, null, 0, "John");
        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(shortUrl));
        when(entityMapper.toShortUrlDTO(any())).thenReturn(dto);

        // Act
        List<ShortUrlDTO> result = shortUrlService.getAll();

        // Assert
        assertEquals(1, result.size());
        verify(shortUrlRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    void testCreateShortUrl_Success() {
        // Arrange
        CreateShortUrlDTOResponse response = new CreateShortUrlDTOResponse("abc123");
        when(authService.getCurrentUser()).thenReturn(user);
        when(keyGenerator.generate()).thenReturn("abc123");
        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(entityMapper.toCreateShortUrlDTOResponse(any())).thenReturn(response);

        // Act
        CreateShortUrlDTOResponse result = shortUrlService.createShortUrl(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("abc123", result.shortKey());
        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
    }

    @Test
    void testGetOriginalUrl_Success() {
        // Arrange
        when(shortUrlRepository.findByShortKey("abc123")).thenReturn(Optional.of(shortUrl));

        // Act
        String result = shortUrlService.getOriginalUrl("abc123");

        // Assert
        assertEquals("https://example.com", result);
        assertEquals(1, shortUrl.getClicks());
    }

    @Test
    void testGetOriginalUrl_NotFound() {
        // Arrange
        when(shortUrlRepository.findByShortKey("invalid")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> shortUrlService.getOriginalUrl("invalid"));
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.findById(1L)).thenReturn(Optional.of(shortUrl));

        // Act
        shortUrlService.deleteById(1L);

        // Assert
        verify(shortUrlRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_AccessDenied() {
        // Arrange
        User otherUser = new User(2L, "aaaaa", "bc", "inter@teste.com", "10101010", Role.ROLE_USER);
        when(authService.getCurrentUser()).thenReturn(otherUser);
        when(shortUrlRepository.findById(1L)).thenReturn(Optional.of(shortUrl));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> shortUrlService.deleteById(1L));
        verify(shortUrlRepository, never()).deleteById(any());
    }
}