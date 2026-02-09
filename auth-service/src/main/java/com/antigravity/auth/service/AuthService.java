package com.antigravity.auth.service;

import com.antigravity.auth.dto.AuthRequest;
import com.antigravity.auth.dto.AuthResponse;
import com.antigravity.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);
}
