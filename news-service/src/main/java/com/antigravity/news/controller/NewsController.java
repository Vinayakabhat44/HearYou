package com.antigravity.news.controller;

import com.antigravity.news.dto.NewsResponse;
import com.antigravity.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/local")
    public NewsResponse getLocalNews(
            @RequestParam(required = false) String village,
            @RequestParam(required = false) String taluk,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String language) {
        return newsService.getLocalNews(village, taluk, district, state, language);
    }
}
