package com.antigravity.feed.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "social-service")
public interface SocialClient {

    @GetMapping("/api/social/friends/{userId}/list")
    List<Long> getFriendIds(@PathVariable("userId") Long userId);
}
