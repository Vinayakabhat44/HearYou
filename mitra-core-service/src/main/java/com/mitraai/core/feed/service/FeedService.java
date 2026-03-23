package com.mitraai.core.feed.service;

import com.mitraai.core.feed.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    Page<Story> getHierarchicalFeed(Double lat, Double lng, Pageable pageable);
}
