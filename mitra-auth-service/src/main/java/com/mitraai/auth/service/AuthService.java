package com.mitraai.auth.service;

import com.mitraai.auth.dto.AuthRequest;
import com.mitraai.auth.dto.AuthResponse;
import com.mitraai.auth.dto.RegisterRequest;
import java.util.Map;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);

    void updatePreferences(Long userId, Map<String, String> preferences);

    Map<String, String> getPreferences(Long userId);
}
