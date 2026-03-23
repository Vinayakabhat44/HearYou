package com.mitraai.core.news.service;

import com.mitraai.core.client.AuthServiceClient;
import com.mitraai.core.dto.UserLocationDTO;
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
        log.info("Current user principal/credentials for location: {}", principal);

        if (!(principal instanceof Long userId)) {
            log.warn("UserId not found in SecurityContext, principal is of type: {}",
                    principal != null ? principal.getClass().getName() : "null");
            return null;
        }

        // 1. Try Cache
        UserLocationDTO cached = (UserLocationDTO) redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + userId);
        if (cached != null) {
            log.info("Location found in cache for userId {}", userId);
            return cached;
        }

        // 2. Try Auth Service
        try {
            log.info("Fetching location from auth-service for userId {}", userId);
            UserLocationDTO location = authServiceClient.getUser(userId);
            if (location != null) {
                log.info("Successfully fetched location for userId {}: {}", userId, location);
                redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + userId, location, Duration.ofHours(1));
                return location;
            } else {
                log.warn("Auth-service returned null location for userId {}", userId);
            }
        } catch (Exception e) {
            log.error("Error fetching location from auth-service for userId {}: {}", userId, e.getMessage());
        }
        return null;
    }
}
