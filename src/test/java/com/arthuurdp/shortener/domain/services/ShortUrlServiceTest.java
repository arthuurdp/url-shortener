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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private static final Long USER_ID = 1L;
    private static final Long OTHER_USER_ID = 2L;
    private static final Long URL_ID = 1L;
    private static final String SHORT_KEY = "abc123";
    private static final String ORIGINAL_URL = "https://example.com";

    private User user;
    private ShortUrl shortUrl;
    private CreateShortUrlDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User(USER_ID, "User", "Test",
                "user@test.com", "password", Role.ROLE_USER);

        shortUrl = new ShortUrl(SHORT_KEY, ORIGINAL_URL, user);
        createDTO = new CreateShortUrlDTO(ORIGINAL_URL);
    }

    @Test
    void testGetAll_UserRole() {
        // Arrange
        ShortUrlDTO dto =
                new ShortUrlDTO(URL_ID, SHORT_KEY, ORIGINAL_URL,
                        null, null, 0, "User");

        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.findAllByUserId(eq(USER_ID), any()))
                .thenReturn(new PageImpl<>(List.of(shortUrl)));
        when(entityMapper.toShortUrlDTO(shortUrl)).thenReturn(dto);

        // Act
        Page<ShortUrlDTO> result = shortUrlService.getAll(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(shortUrlRepository).findAllByUserId(eq(USER_ID), any());
        verify(entityMapper).toShortUrlDTO(shortUrl);
    }

    @Test
    void testCreateShortUrl_Success() {
        // Arrange
        CreateShortUrlDTOResponse response = new CreateShortUrlDTOResponse(SHORT_KEY);

        when(authService.getCurrentUser()).thenReturn(user);
        when(keyGenerator.generate()).thenReturn(SHORT_KEY);
        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenReturn(shortUrl);
        when(entityMapper.toCreateShortUrlDTOResponse(any(ShortUrl.class)))
                .thenReturn(response);

        // Act
        CreateShortUrlDTOResponse result = shortUrlService.createShortUrl(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals(SHORT_KEY, result.shortKey());

        verify(keyGenerator).generate();
        verify(shortUrlRepository).save(any(ShortUrl.class));
    }

    @Test
    void testGetOriginalUrl_Success() {
        // Arrange
        when(shortUrlRepository.findByShortKey(SHORT_KEY))
                .thenReturn(Optional.of(shortUrl));

        // Act
        String result = shortUrlService.getOriginalUrl(SHORT_KEY);

        // Assert
        assertEquals(ORIGINAL_URL, result);
        assertEquals(1, shortUrl.getClicks());

        verify(shortUrlRepository).findByShortKey(SHORT_KEY);
    }

    @Test
    void testGetOriginalUrl_NotFound() {
        // Arrange
        when(shortUrlRepository.findByShortKey("invalid"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> shortUrlService.getOriginalUrl("invalid"));

        verify(shortUrlRepository).findByShortKey("invalid");
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.findById(URL_ID))
                .thenReturn(Optional.of(shortUrl));

        // Act
        shortUrlService.deleteById(URL_ID);

        // Assert
        verify(shortUrlRepository).deleteById(URL_ID);
    }

    @Test
    void testDeleteById_ResourceNotFound() {
        // Arrange
        User otherUser = new User(
                OTHER_USER_ID,
                "Other",
                "User",
                "other@test.com",
                "password",
                Role.ROLE_USER
        );

        when(authService.getCurrentUser()).thenReturn(otherUser);
        when(shortUrlRepository.findById(URL_ID))
                .thenReturn(Optional.of(shortUrl));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> shortUrlService.deleteById(URL_ID));

        verify(shortUrlRepository, never()).deleteById(any());
    }
}
