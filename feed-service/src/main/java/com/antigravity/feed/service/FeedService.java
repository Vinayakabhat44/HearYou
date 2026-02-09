package com.antigravity.feed.service;

import com.antigravity.feed.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Page<Story> getHierarchicalFeed(Double lat, Double lng, Pageable pageable);
}
