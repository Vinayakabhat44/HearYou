package com.mitraai.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "mitra-core-service", contextId = "socialClient")
public interface SocialClient {

    @GetMapping("/api/social/friends/{userId}/ids")
    List<Long> getFriendIds(@PathVariable("userId") Long userId);
}
