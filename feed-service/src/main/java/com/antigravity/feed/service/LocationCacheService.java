package com.antigravity.feed.service;

import com.antigravity.feed.dto.UserLocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LocationCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "user:location:";
    private static final long CACHE_TTL_HOURS = 24;

    public void cacheUserLocation(UserLocationDTO location) {
        String key = CACHE_KEY_PREFIX + location.getId();
        redisTemplate.opsForValue().set(key, location, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    public UserLocationDTO getUserLocation(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        return (UserLocationDTO) redisTemplate.opsForValue().get(key);
    }

    public void evictUserLocation(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
