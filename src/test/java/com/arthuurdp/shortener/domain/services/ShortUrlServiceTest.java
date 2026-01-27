//package com.arthuurdp.shortener.domain.services;
//
//import com.arthuurdp.shortener.domain.entities.url.ShortUrl;
//import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
//import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
//import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
//import com.arthuurdp.shortener.domain.repositories.ShortUrlRepository;
//import com.arthuurdp.shortener.domain.services.exceptions.ResourceNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ShortUrlServiceTest {
//
//    @Mock
//    private ShortUrlRepository repository;
//
//    @Mock
//    private EntityMapperService entityMapper;
//
//    @Mock
//    private ShortKeyGeneratorService keyGenerator;
//
//    @InjectMocks
//    private ShortUrlService service;
//
//    private ShortUrl shortUrl;
//    private CreateShortUrlDTOResponse createShortUrlDTOResponse;
//
//    @BeforeEach
//    void setUp() {
//        shortUrl = new ShortUrl();
//        shortUrl.setOriginalUrl("https://example.com");
//        shortUrl.setShortKey("abc123");
//        shortUrl.setCreatedAt(Instant.now());
//        shortUrl.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
//
//        createShortUrlDTOResponse = new CreateShortUrlDTOResponse(
//                "abc123",
//                "https://example.com",
//                shortUrl.getExpiresAt()
//        );
//    }
//
//    @Test
//    void testGetAll() {
//        List<ShortUrl> urlList = new ArrayList<>();
//        urlList.add(shortUrl);
//        when(repository.findAll()).thenReturn(urlList);
//        when(entityMapper.toCreateShortUrlDTOResponse(shortUrl)).thenReturn(createShortUrlDTOResponse);
//
//        List<ShortUrlDTO> result = service.getAll();
//
//        assertEquals(1, result.size());
//        assertEquals("abc123", result.get(0).shortKey());
//        verify(repository).findAll();
//    }
//
//    @Test
//    void testCreateShortUrl() {
//
//        CreateShortUrlDTO createDTO = new CreateShortUrlDTO("https://example.com");
//        when(keyGenerator.generate()).thenReturn("abc123");
//        when(repository.save(any(ShortUrl.class))).thenReturn(shortUrl);
//        when(entityMapper.toCreateShortUrlDTOResponse(any(ShortUrl.class))).thenReturn(createShortUrlDTOResponse);
//
//        CreateShortUrlDTOResponse result = service.createShortUrl(createDTO);
//
//        assertNotNull(result);
//        assertEquals("abc123", result.shortKey());
//        assertEquals("https://example.com", result.originalUrl());
//        verify(keyGenerator).generate();
//        verify(repository).save(any(ShortUrl.class));
//    }
//
//    @Test
//    void testGetOriginalUrl() {
//        when(repository.findByShortKey("abc123")).thenReturn(Optional.of(shortUrl));
//
//        String result = service.getOriginalUrl("abc123");
//
//        assertEquals("https://example.com", result);
//        verify(repository).findByShortKey("abc123");
//    }
//
//    @Test
//    void testGetOriginalUrlThrowsException() {
//        when(repository.findByShortKey("notfound")).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            service.getOriginalUrl("notfound");
//        });
//        verify(repository).findByShortKey("notfound");
//    }
//
//    @Test
//    void testDeleteByShortKey() {
//        String shortKey = "abc123";
//
//        service.deleteByShortKey(shortKey);
//
//        verify(repository).deleteByShortKey(shortKey);
//    }
//
//    @Test
//    void testDeleteExpiredUrls() {
//        service.deleteByExpiresAtBefore();
//
//        verify(repository).deleteByExpiresAtBefore(any(Instant.class));
//    }
//}