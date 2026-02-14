package com.antigravity.auth.service;

import com.antigravity.auth.dto.AuthRequest;
import com.antigravity.auth.dto.AuthResponse;
import com.antigravity.auth.dto.RegisterRequest;
import java.util.Map;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);

    void updatePreferences(Long userId, Map<String, String> preferences);

    Map<String, String> getPreferences(Long userId);
}
