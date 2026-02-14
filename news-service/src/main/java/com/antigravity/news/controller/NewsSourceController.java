package com.antigravity.news.controller;

import com.antigravity.news.entity.NewsSource;
import com.antigravity.news.repository.NewsSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/sources")
@RequiredArgsConstructor
public class NewsSourceController {

    private final NewsSourceRepository sourceRepository;

    @GetMapping
    public ResponseEntity<List<NewsSource>> getAllSources() {
        return ResponseEntity.ok(sourceRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<NewsSource> addSource(@RequestBody NewsSource source) {
        if (sourceRepository.existsByUrl(source.getUrl())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(sourceRepository.save(source));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<NewsSource>> addSourcesBulk(@RequestBody List<NewsSource> sources) {
        // Filter out existing URLs to avoid duplicates
        List<NewsSource> newSources = sources.stream()
                .filter(s -> !sourceRepository.existsByUrl(s.getUrl()))
                .toList();

        if (newSources.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(sourceRepository.saveAll(newSources));
    }
}
