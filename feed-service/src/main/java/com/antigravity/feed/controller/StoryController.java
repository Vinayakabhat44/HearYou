package com.antigravity.feed.controller;

import com.antigravity.feed.entity.Story;
import com.antigravity.feed.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Story> createStory(
            @RequestPart("story") Story story,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            Authentication authentication) {
        Long userId = resolveUserId(authentication);
        return ResponseEntity.ok(storyService.createStory(story, lat, lng, userId, file));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Story> createStoryJson(
            @RequestBody Story story,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            Authentication authentication) {
        Long userId = resolveUserId(authentication);
        return ResponseEntity.ok(storyService.createStory(story, lat, lng, userId, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id, Authentication authentication) {
        Long userId = resolveUserId(authentication);
        storyService.deleteStory(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        String name = authentication.getName();
        try {
            return Long.parseLong(name);
        } catch (NumberFormatException e) {
            // Fallback for old tokens that have username as subject
            return storyService.resolveUserIdByUsername(name);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.getStory(id));
    }
}
