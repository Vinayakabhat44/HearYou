package com.antigravity.feed.service;

import com.antigravity.feed.entity.Story;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {
    Story createStory(Story story, Double lat, Double lng, Long userId, MultipartFile file);

    Story getStory(Long id);

    List<Story> getHierarchicalFeed(Long userId);

    void deleteStory(Long id, Long userId);
}
