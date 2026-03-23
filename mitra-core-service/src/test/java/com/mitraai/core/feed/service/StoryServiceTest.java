package com.mitraai.core.feed.service;

import com.mitraai.core.client.AuthServiceClient;
import com.mitraai.core.client.MediaServiceClient;
import com.mitraai.core.client.SocialClient;
import com.mitraai.core.dto.LocationData;
import com.mitraai.core.dto.UserLocationDTO;
import com.mitraai.core.feed.entity.Story;
import com.mitraai.core.feed.repository.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private LocationCacheService locationCacheService;

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private MediaServiceClient mediaServiceClient;

    @Mock
    private SocialClient socialClient;

    @InjectMocks
    private StoryServiceImpl storyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStory_withCoordinates_shouldPopulateLocationData() {
        // Arrange
        Story story = new Story();
        story.setContent("Test Content");
        Double lat = 12.34;
        Double lng = 77.56;
        Long userId = 1L;

        LocationData locationData = new LocationData("Village", "Taluk", "District", "State");
        when(geocodingService.reverseGeocode(lat, lng)).thenReturn(locationData);
        when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Story savedStory = storyService.createStory(story, lat, lng, userId, null);

        // Assert
        assertNotNull(savedStory);
        assertEquals(userId, savedStory.getUserId());
        assertEquals("Village", savedStory.getVillage());
        assertEquals("Taluk", savedStory.getTaluk());
        assertEquals("District", savedStory.getDistrict());
        assertEquals("State", savedStory.getState());
        assertNotNull(savedStory.getLocation());
        assertEquals(lng, savedStory.getLocation().getX());
        assertEquals(lat, savedStory.getLocation().getY());
        verify(geocodingService).reverseGeocode(lat, lng);
    }

    @Test
    void createStory_withoutCoordinates_shouldUseUserProfile() {
        // Arrange
        Story story = new Story();
        Long userId = 1L;

        UserLocationDTO userProfile = new UserLocationDTO();
        userProfile.setId(userId);
        userProfile.setVillage("HomeVillage");
        userProfile.setDistrict("HomeDistrict");
        userProfile.setLatitude(10.0);
        userProfile.setLongitude(20.0);

        when(locationCacheService.getUserLocation(userId)).thenReturn(null);
        when(authServiceClient.getUser(userId)).thenReturn(userProfile);
        when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Story savedStory = storyService.createStory(story, null, null, userId, null);

        // Assert
        assertEquals("HomeVillage", savedStory.getVillage());
        assertEquals("HomeDistrict", savedStory.getDistrict());
        assertNotNull(savedStory.getLocation());
        assertEquals(20.0, savedStory.getLocation().getX());
        assertEquals(10.0, savedStory.getLocation().getY());
    }

    @Test
    void createStory_withMedia_shouldUploadFile() {
        // Arrange
        Story story = new Story();
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(mediaServiceClient.uploadFile(any(), anyString())).thenReturn("http://s3.com/file.jpg");
        when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Story savedStory = storyService.createStory(story, null, null, userId, file);

        // Assert
        assertEquals("http://s3.com/file.jpg", savedStory.getMediaUrl());
        verify(mediaServiceClient).uploadFile(eq(file), contains("story"));
    }
}
