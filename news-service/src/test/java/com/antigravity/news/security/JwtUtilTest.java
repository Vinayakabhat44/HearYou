package com.antigravity.news.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        ReflectionTestUtils.setField(jwtUtil, "publicKey", keyPair.getPublic());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Test
    void extractUserId_asInteger_shouldReturnLong() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123); // Integer
        String token = createToken(claims, "testuser");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(123L, userId);
    }

    @Test
    void extractUserId_asLong_shouldReturnLong() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123L); // Long
        String token = createToken(claims, "testuser");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(123L, userId);
    }

    @Test
    void extractUserId_asString_shouldReturnLong() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "123"); // String
        String token = createToken(claims, "testuser");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(123L, userId);
    }

    @Test
    void extractUserId_missing_shouldReturnNull() {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, "testuser");

        Long userId = jwtUtil.extractUserId(token);

        assertNull(userId);
    }

    @Test
    void validateToken_validToken_shouldReturnTrue() {
        String token = createToken(new HashMap<>(), "testuser");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_expiredToken_shouldReturnFalse() {
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        assertFalse(jwtUtil.validateToken(token));
    }
}
