package com.antigravity.feed.controller;

import com.antigravity.feed.entity.Story;
import com.antigravity.feed.service.StoryService;
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
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(storyService.getHierarchicalFeed(userId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Story>> getFriendsFeed(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(storyService.getFriendsFeed(userId));
    }
}
