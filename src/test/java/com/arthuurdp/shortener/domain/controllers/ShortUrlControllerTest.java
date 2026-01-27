package com.arthuurdp.shortener.domain.controllers;

import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.url.CreateShortUrlDTOResponse;
import com.arthuurdp.shortener.domain.entities.url.ShortUrlDTO;
import com.arthuurdp.shortener.domain.services.ShortUrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShortUrlService service;

    @Test
    void getAllShouldReturnListOfShortUrlDTO() throws Exception {
        ShortUrlDTO dto = new ShortUrlDTO(1L, "abc", "https://google.com", Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS));
        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shortKey").value("abc"))
                .andExpect(jsonPath("$[0].originalUrl").value("https://google.com"));

        verify(service, times(1)).getAll();
    }

    @Test
    void redirectShouldReturnFoundAndLocation() throws Exception {
        String shortKey = "abc";
        String originalUrl = "https://google.com";
        when(service.getOriginalUrl(shortKey)).thenReturn(originalUrl);

        mockMvc.perform(get("/{shortKey}", shortKey))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));

        verify(service, times(1)).getOriginalUrl(shortKey);
    }

    @Test
    void createShortUrlShouldReturnCreated() throws Exception {
        CreateShortUrlDTO createDTO = new CreateShortUrlDTO("https://google.com");
        CreateShortUrlDTOResponse responseDTO = new CreateShortUrlDTOResponse("abc");

        when(service.createShortUrl(any(CreateShortUrlDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.shortKey").value("abc"))
                .andExpect(jsonPath("$.originalUrl").value("https://google.com"));

        verify(service, times(1)).createShortUrl(any(CreateShortUrlDTO.class));
    }

//    @Test
//    void deleteByShortKeyShouldReturnNoContent() throws Exception {
//        String shortKey = "abc";
//        doNothing().when(service).deleteByShortKey(shortKey);
//
//        mockMvc.perform(delete("/{shortKey}", shortKey))
//                .andExpect(status().isNoContent());
//
//        verify(service, times(1)).deleteByShortKey(shortKey);
//    }
}