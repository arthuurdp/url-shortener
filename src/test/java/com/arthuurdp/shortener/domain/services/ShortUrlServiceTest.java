package com.arthuurdp.shortener.domain.services;

import com.arthuurdp.shortener.domain.entities.enums.Role;
import com.arthuurdp.shortener.domain.entities.url.*;
import com.arthuurdp.shortener.domain.entities.user.User;
import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private static final Long USER1_ID = 1L;
    private static final Long USER2_ID = 2L;
    private static final Long URL_ID = 1L;
    private static final String SHORT_KEY = "abc123";
    private static final String ORIGINAL_URL = "https://example.com";

    private User user;
    private ShortUrl shortUrl;
    private CreateShortUrlDTO createDTO;

    @BeforeEach
    void setUp() {
        user = new User(USER1_ID, "User", "Test",
                "user@test.com", "password", Role.ROLE_USER);

        shortUrl = new ShortUrl(ORIGINAL_URL, user);
        createDTO = new CreateShortUrlDTO(ORIGINAL_URL, null);
    }

    @Test
    void testGetAll_UserRole() {
        ShortUrlDTO dto =
                new ShortUrlDTO(URL_ID, SHORT_KEY, ORIGINAL_URL,
                        null, null, 0, "User");

        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.findAllByUserId(eq(USER1_ID), any()))
                .thenReturn(new PageImpl<>(List.of(shortUrl)));
        when(entityMapper.toShortUrlDTO(shortUrl)).thenReturn(dto);

        Page<ShortUrlDTO> result = shortUrlService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(shortUrlRepository).findAllByUserId(eq(USER1_ID), any());
        verify(entityMapper).toShortUrlDTO(shortUrl);
    }

    @Test
    void testCreateShortUrl_Success() throws Exception {
        CreateShortUrlDTOResponse response = new CreateShortUrlDTOResponse(SHORT_KEY);

        when(authService.getCurrentUser()).thenReturn(user);

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenAnswer(invocation -> {
                    ShortUrl url = invocation.getArgument(0);

                    Field idField = ShortUrl.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(url, URL_ID);

                    return url;
                });

        when(keyGenerator.encode(URL_ID)).thenReturn(SHORT_KEY);

        when(entityMapper.toCreateShortUrlDTOResponse(any(ShortUrl.class)))
                .thenReturn(response);

        CreateShortUrlDTOResponse result =
                shortUrlService.createShortUrl(createDTO);

        assertNotNull(result);
        assertEquals(SHORT_KEY, result.shortKey());

        verify(shortUrlRepository).save(any());
        verify(keyGenerator).encode(URL_ID);
    }

    @Test
    void testCreateShortUrl_WithCustomKey_Success() {
        String customKey = "my-custom-key";
        CreateShortUrlDTO customDTO = new CreateShortUrlDTO(ORIGINAL_URL, customKey);
        CreateShortUrlDTOResponse response = new CreateShortUrlDTOResponse(customKey);

        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.existsByShortKey(customKey)).thenReturn(false);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(entityMapper.toCreateShortUrlDTOResponse(any(ShortUrl.class))).thenReturn(response);

        CreateShortUrlDTOResponse result = shortUrlService.createShortUrl(customDTO);

        assertEquals(customKey, result.shortKey());
        verify(shortUrlRepository).existsByShortKey(customKey);
        verify(shortUrlRepository).save(any());
        verify(keyGenerator, never()).encode(anyLong());
    }

    @Test
    void testCreateShortUrl_WithCustomKey_AlreadyInUse() {
        String customKey = "already-taken";
        CreateShortUrlDTO customDTO = new CreateShortUrlDTO(ORIGINAL_URL, customKey);

        when(authService.getCurrentUser()).thenReturn(user);
        when(shortUrlRepository.existsByShortKey(customKey)).thenReturn(true);

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class,
                () -> shortUrlService.createShortUrl(customDTO));

        verify(shortUrlRepository, never()).save(any());
    }

    @Test
    void testGetOriginalUrl_Success() {
        when(shortUrlRepository.findByShortKey(SHORT_KEY)).thenReturn(Optional.of(shortUrl));

        String result = shortUrlService.getOriginalUrl(SHORT_KEY);

        assertEquals(ORIGINAL_URL, result);
        assertEquals(1, shortUrl.getClicks());

        verify(shortUrlRepository).findByShortKey(SHORT_KEY);
    }

    @Test
    void testGetOriginalUrl_NotFound() {
        when(shortUrlRepository.findByShortKey("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> shortUrlService.getOriginalUrl("invalid"));

        verify(shortUrlRepository).findByShortKey("invalid");
    }

    @Test
    void testDeleteById_Success() {
        when(shortUrlRepository.findById(URL_ID)).thenReturn(Optional.of(shortUrl));

        shortUrlService.deleteById(URL_ID);

        verify(shortUrlRepository).deleteById(URL_ID);
    }

    @Test
    void testDeleteById_ResourceNotFound() {
        when(shortUrlRepository.findById(URL_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> shortUrlService.deleteById(URL_ID));

        verify(shortUrlRepository, never()).deleteById(any());
    }
}
