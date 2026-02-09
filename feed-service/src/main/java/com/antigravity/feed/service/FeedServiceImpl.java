package com.antigravity.feed.service;

import com.antigravity.feed.dto.LocationData;
import com.antigravity.feed.entity.Story;
import com.antigravity.feed.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final StoryRepository storyRepository;
    private final GeocodingService geocodingService;

    @Override
    public Page<Story> getHierarchicalFeed(Double lat, Double lng, Pageable pageable) {
        LocationData location = geocodingService.reverseGeocode(lat, lng);

        // Use the smart hierarchical query for efficient pagination
        return storyRepository.findHierarchical(
                location.getVillage(),
                location.getTaluk(),
                location.getDistrict(),
                pageable);
    }
}
