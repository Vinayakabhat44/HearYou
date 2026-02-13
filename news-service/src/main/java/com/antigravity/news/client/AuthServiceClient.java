package com.antigravity.news.client;

import com.antigravity.news.dto.UserLocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @GetMapping("/api/users/{id}")
    UserLocationDTO getUser(@PathVariable("id") Long id);
}
