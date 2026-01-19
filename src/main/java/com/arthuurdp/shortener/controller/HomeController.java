package com.arthuurdp.shortener.controller;

import com.arthuurdp.shortener.entities.models.ShortUrlDTO;
import com.arthuurdp.shortener.services.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    ShortUrlService shortUrlService;

    public HomeController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDTO> publicShortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", publicShortUrls);
        model.addAttribute("baseUrl", "http://localhost:8080");
        return "index";
    }
}
