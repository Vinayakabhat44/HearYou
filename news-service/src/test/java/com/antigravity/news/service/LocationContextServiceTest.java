package com.antigravity.news.service;

import com.antigravity.news.client.AuthServiceClient;
import com.antigravity.news.dto.UserLocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LocationContextServiceTest {

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LocationContextService locationContextService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getCurrentUserLocation_noCredentials_shouldReturnNull() {
        when(authentication.getCredentials()).thenReturn(null);

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNull(result);
    }

    @Test
    void getCurrentUserLocation_invalidCredentialType_shouldReturnNull() {
        when(authentication.getCredentials()).thenReturn("not-a-long");

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNull(result);
    }

    @Test
    void getCurrentUserLocation_hitCache_shouldReturnCachedLocation() {
        Long userId = 1L;
        UserLocationDTO cachedLocation = UserLocationDTO.builder().taluk("TestTaluk").build();
        when(authentication.getCredentials()).thenReturn(userId);
        when(valueOperations.get(anyString())).thenReturn(cachedLocation);

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNotNull(result);
        assertEquals("TestTaluk", result.getTaluk());
        verify(authServiceClient, never()).getUser(anyLong());
    }

    @Test
    void getCurrentUserLocation_authServiceReturnsNull_shouldReturnNull() {
        Long userId = 1L;
        when(authentication.getCredentials()).thenReturn(userId);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(authServiceClient.getUser(userId)).thenReturn(null);

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNull(result);
    }

    @Test
    void getCurrentUserLocation_authServiceThrowsException_shouldReturnNullSafely() {
        Long userId = 1L;
        when(authentication.getCredentials()).thenReturn(userId);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(authServiceClient.getUser(userId)).thenThrow(new RuntimeException("Service down"));

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNull(result);
    }

    @Test
    void getCurrentUserLocation_success_shouldCacheAndReturn() {
        Long userId = 1L;
        UserLocationDTO location = UserLocationDTO.builder().taluk("Taluk").build();
        when(authentication.getCredentials()).thenReturn(userId);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(authServiceClient.getUser(userId)).thenReturn(location);

        UserLocationDTO result = locationContextService.getCurrentUserLocation();

        assertNotNull(result);
        assertEquals("Taluk", result.getTaluk());
        verify(valueOperations).set(eq("userLocation:" + userId), eq(location), any());
    }
}
