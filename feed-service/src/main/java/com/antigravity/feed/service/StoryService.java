package com.antigravity.feed.service;

import com.antigravity.feed.entity.Story;

import java.util.List;

public interface StoryService {
    Story createStory(Story story, Double lat, Double lng, Long userId);

    Story getStory(Long id);

    List<Story> getHierarchicalFeed(Long userId);
}
