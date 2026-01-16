package com.arthuurdp.shortener.controller;

import com.arthuurdp.shortener.entities.ShortUrl;
import com.arthuurdp.shortener.services.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ShortUrlService shortUrlService;

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrl> publicShortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", publicShortUrls);
        model.addAttribute("baseUrl", "http://localhost:8080");
        return "index";
    }
}
