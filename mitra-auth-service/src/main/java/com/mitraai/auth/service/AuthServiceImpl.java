package com.mitraai.auth.service;

import com.mitraai.auth.dto.AuthRequest;
import com.mitraai.auth.dto.AuthResponse;
import com.mitraai.auth.dto.RegisterRequest;

import com.mitraai.auth.entity.User;
import java.util.Map;
import java.util.HashMap;
import com.mitraai.auth.repository.UserRepository;
import com.mitraai.auth.security.CustomUserDetails;
import com.mitraai.auth.security.JwtUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GeocodingService geocodingService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public AuthResponse login(AuthRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Find user in database using dual lookup (username or mobile)
        User user = userRepository.findByUsernameOrMobileNumber(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getVillage(),
                user.getTaluk(),
                user.getDistrict(),
                user.getState(),
                user.getPincode(),
                user.getHomeLocation() != null ? user.getHomeLocation().getY() : null,
                user.getHomeLocation() != null ? user.getHomeLocation().getX() : null,
                user.getPreferences()
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Validate mandatory mobile number
        if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {
            throw new RuntimeException("Mobile number is mandatory");
        }

        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty() && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (request.getMobileNumber() != null && userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        // Set location fields if provided
        if (request.getVillage() != null)
            user.setVillage(request.getVillage());
        if (request.getTaluk() != null)
            user.setTaluk(request.getTaluk());
        if (request.getDistrict() != null)
            user.setDistrict(request.getDistrict());
        if (request.getState() != null)
            user.setState(request.getState());
        if (request.getPincode() != null)
            user.setPincode(request.getPincode());

        // Handle Coordinates & Automatic Geocoding
        if (request.getLatitude() != null && request.getLongitude() != null) {
            double lat = request.getLatitude();
            double lng = request.getLongitude();

            // Set Geometry Point
            Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
            user.setHomeLocation(point);

            // Fetch address details if missing
            if (user.getVillage() == null || user.getVillage().isEmpty() ||
                    user.getPincode() == null || user.getPincode().isEmpty()) {

                com.mitraai.auth.dto.LocationData geoInfo = geocodingService.reverseGeocode(lat, lng);

                if (user.getVillage() == null || user.getVillage().isEmpty())
                    user.setVillage(geoInfo.getVillage());
                if (user.getTaluk() == null || user.getTaluk().isEmpty())
                    user.setTaluk(geoInfo.getTaluk());
                if (user.getDistrict() == null || user.getDistrict().isEmpty())
                    user.setDistrict(geoInfo.getDistrict());
                if (user.getState() == null || user.getState().isEmpty())
                    user.setState(geoInfo.getState());
                if (user.getPincode() == null || user.getPincode().isEmpty())
                    user.setPincode(geoInfo.getPincode());
            }
        }

        // Save user
        user = userRepository.save(user);

        // Generate JWT token
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getVillage(),
                user.getTaluk(),
                user.getDistrict(),
                user.getState(),
                user.getPincode(),
                user.getHomeLocation() != null ? user.getHomeLocation().getY() : null,
                user.getHomeLocation() != null ? user.getHomeLocation().getX() : null,
                user.getPreferences()
        );
    }

    @Override
    public void updatePreferences(Long userId, Map<String, String> preferences) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPreferences() == null) {
            user.setPreferences(new HashMap<>());
        }
        user.getPreferences().putAll(preferences);
        userRepository.save(user);
    }

    @Override
    public Map<String, String> getPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPreferences();
    }
}
