package com.mitraai.core.news.controller;

import com.mitraai.core.dto.LocalizedFeedResponse;
import com.mitraai.core.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/local-feed")
    public LocalizedFeedResponse getLocalFeed(
            @RequestParam(required = false) String pincode,
            @RequestParam(required = false) String taluk,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String category) {
        return newsService.getLocalFeed(pincode, taluk, district, state);
    }
}
