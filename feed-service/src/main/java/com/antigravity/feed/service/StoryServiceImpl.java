package com.antigravity.feed.service;

import com.antigravity.feed.annotation.AuthorizeOwner;
import com.antigravity.feed.annotation.AuditLog;
import com.antigravity.feed.annotation.Retry;
import com.antigravity.feed.annotation.TrackTime;
import com.antigravity.feed.client.AuthServiceClient;
import com.antigravity.feed.client.MediaServiceClient;
import com.antigravity.feed.dto.LocationData;
import com.antigravity.feed.dto.UserLocationDTO;
import com.antigravity.feed.entity.Story;
import com.antigravity.feed.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final GeocodingService geocodingService;
    private final LocationCacheService locationCacheService;
    private final AuthServiceClient authServiceClient;
    private final MediaServiceClient mediaServiceClient;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    @TrackTime
    @AuditLog("CREATE_STORY")
    public Story createStory(Story story, Double lat, Double lng, Long userId, MultipartFile file) {
        story.setUserId(userId);

        // 1. Handle Media Upload
        if (file != null && !file.isEmpty()) {
            String folder = story.getType().toString().toLowerCase() + "s";
            String fileUrl = mediaServiceClient.uploadFile(file, folder);
            story.setMediaUrl(fileUrl);
        }

        // 2. Handle Location
        if (lat != null && lng != null) {
            // Use provided coordinates (Geo-tagged)
            Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
            story.setLocation(point);

            LocationData locationData = geocodingService.reverseGeocode(lat, lng);
            story.setVillage(locationData.getVillage());
            story.setTaluk(locationData.getTaluk());
            story.setDistrict(locationData.getDistrict());
            story.setState(locationData.getState());
        } else {
            // Internal lookup for registered home location
            UserLocationDTO userLocation = getUserLocation(userId);
            if (userLocation != null) {
                if (userLocation.getLongitude() != null && userLocation.getLatitude() != null) {
                    Point point = geometryFactory
                            .createPoint(new Coordinate(userLocation.getLongitude(), userLocation.getLatitude()));
                    story.setLocation(point);
                }
                story.setVillage(userLocation.getVillage());
                story.setTaluk(userLocation.getTaluk());
                story.setDistrict(userLocation.getDistrict());
                story.setState(userLocation.getState());
            }
        }

        return storyRepository.save(story);
    }

    @Retry(attempts = 2, delay = 500)
    private UserLocationDTO getUserLocation(Long userId) {
        // Try Cache
        UserLocationDTO cached = locationCacheService.getUserLocation(userId);
        if (cached != null) {
            return cached;
        }

        // Try Auth Service
        try {
            UserLocationDTO location = authServiceClient.getUser(userId);
            if (location != null) {
                locationCacheService.cacheUserLocation(location);
                return location;
            }
        } catch (Exception e) {
            // Handle service failure (e.g., fallback to defaults or log)
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Story getStory(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
    }

    @Override
    @TrackTime
    public List<Story> getHierarchicalFeed(Long userId) {
        UserLocationDTO userLocation = getUserLocation(userId);
        if (userLocation == null) {
            return List.of();
        }

        // Default to first 20 stories for hierarchical feed
        return storyRepository.findHierarchical(
                userLocation.getVillage(),
                userLocation.getTaluk(),
                userLocation.getDistrict(),
                PageRequest.of(0, 20)).getContent();
    }

    @Override
    @AuthorizeOwner
    @AuditLog("DELETE_STORY")
    public void deleteStory(Long id, Long userId) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
        storyRepository.delete(story);
    }
}
