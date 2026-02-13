package com.antigravity.news.service;

import com.antigravity.news.client.AuthServiceClient;
import com.antigravity.news.dto.UserLocationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationContextService {

    private final AuthServiceClient authServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "userLocation:";

    public UserLocationDTO getCurrentUserLocation() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (!(principal instanceof Long userId)) {
            return null;
        }

        // 1. Try Cache
        UserLocationDTO cached = (UserLocationDTO) redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + userId);
        if (cached != null) {
            return cached;
        }

        // 2. Try Auth Service
        try {
            UserLocationDTO location = authServiceClient.getUser(userId);
            if (location != null) {
                redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + userId, location, Duration.ofHours(1));
                return location;
            }
        } catch (Exception e) {
            log.error("Error fetching location from auth-service for userId {}: {}", userId, e.getMessage());
        }
        return null;
    }
}
