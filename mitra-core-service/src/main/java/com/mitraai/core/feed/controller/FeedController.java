package com.mitraai.core.feed.controller;

import com.mitraai.core.feed.entity.Story;
import com.mitraai.core.feed.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final StoryService storyService;

    @GetMapping("/hierarchical")
    public ResponseEntity<List<Story>> getHierarchicalFeed(Authentication authentication) {
        Long userId = resolveUserId(authentication);
        return ResponseEntity.ok(storyService.getHierarchicalFeed(userId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Story>> getFriendsFeed(Authentication authentication) {
        Long userId = resolveUserId(authentication);
        return ResponseEntity.ok(storyService.getFriendsFeed(userId));
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        String name = authentication.getName();
        try {
            return Long.parseLong(name);
        } catch (NumberFormatException e) {
            // Fallback for old tokens or usernames as subject
            return storyService.resolveUserIdByUsername(name);
        }
    }
}
