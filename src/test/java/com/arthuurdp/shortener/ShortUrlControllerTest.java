package com.arthuurdp.shortener;

import com.arthuurdp.shortener.domain.controllers.ShortUrlController;
import com.arthuurdp.shortener.domain.entities.dtos.CreateShortUrlDTO;
import com.arthuurdp.shortener.domain.entities.dtos.ShortUrlDTO;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturn200AndList() throws Exception {
        ShortUrlDTO dto = new ShortUrlDTO("abc123", "https://google.com", Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS));

        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shortKey").value("abc123"))
                .andExpect(jsonPath("$[0].originalUrl").value("https://google.com"));
    }

    @Test
    void redirect_shouldReturn302WithLocationHeader() throws Exception {
        when(service.getOriginalUrl("abc123"))
                .thenReturn("https://google.com");

        mockMvc.perform(get("/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://google.com"));
    }

    @Test
    void createShortUrl_shouldReturn201AndBody() throws Exception {
        CreateShortUrlDTO input = new CreateShortUrlDTO("https://google.com");
        ShortUrlDTO output = new ShortUrlDTO("abc123", "https://google.com", Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS));

        when(service.createShortUrl(any())).thenReturn(output);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortKey").value("abc123"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void deleteByShortKey_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/abc123"))
                .andExpect(status().isNoContent());

        verify(service).deleteByShortKey("abc123");
    }
}
