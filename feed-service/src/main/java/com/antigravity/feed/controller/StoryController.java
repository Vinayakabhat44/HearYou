package com.antigravity.feed.controller;

import com.antigravity.feed.entity.Story;
import com.antigravity.feed.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<Story> createStory(
            @RequestBody Story story,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            Authentication authentication) {
        // Extract userId from JWT token
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(storyService.createStory(story, lat, lng, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.getStory(id));
    }
}
