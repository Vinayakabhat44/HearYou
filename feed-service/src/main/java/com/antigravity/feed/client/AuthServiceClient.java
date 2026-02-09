package com.antigravity.feed.client;

import com.antigravity.feed.config.FeignConfig;
import com.antigravity.feed.dto.UserLocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", configuration = FeignConfig.class)
public interface AuthServiceClient {

    @GetMapping("/api/users/{id}")
    UserLocationDTO getUser(@PathVariable("id") Long id);
}
