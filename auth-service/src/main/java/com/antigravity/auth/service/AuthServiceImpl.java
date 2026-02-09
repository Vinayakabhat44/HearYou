package com.antigravity.auth.service;

import com.antigravity.auth.dto.AuthRequest;
import com.antigravity.auth.dto.AuthResponse;
import com.antigravity.auth.dto.RegisterRequest;
import com.antigravity.auth.entity.User;
import com.antigravity.auth.repository.UserRepository;
import com.antigravity.auth.security.JwtUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // Find user in database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
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

                com.antigravity.auth.dto.LocationData geoInfo = geocodingService.reverseGeocode(lat, lng);

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
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}
