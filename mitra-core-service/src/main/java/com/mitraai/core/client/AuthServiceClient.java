package com.mitraai.core.client;

import com.mitraai.core.config.FeignConfig;
import com.mitraai.core.dto.UserLocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mitra-auth-service", configuration = FeignConfig.class)
public interface AuthServiceClient {

    @GetMapping("/api/users/{id}")
    UserLocationDTO getUser(@PathVariable("id") Long id);

    @GetMapping("/api/users/username/{username}")
    UserLocationDTO getUserByUsername(@PathVariable("username") String username);
}
