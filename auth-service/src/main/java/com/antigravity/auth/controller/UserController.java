package com.antigravity.auth.controller;

import com.antigravity.auth.dto.LocationData;
import com.antigravity.auth.entity.User;
import com.antigravity.auth.repository.UserRepository;
import com.antigravity.auth.dto.UpdatePreferencesRequest;
import com.antigravity.auth.service.AuthService;
import com.antigravity.auth.service.GeocodingService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final GeocodingService geocodingService;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @PutMapping("/{id}/location")
    public ResponseEntity<User> updateLocation(
            @PathVariable Long id,
            @RequestParam Double lat,
            @RequestParam Double lng) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
        user.setHomeLocation(point);

        LocationData locationData = geocodingService.reverseGeocode(lat, lng);
        user.setVillage(locationData.getVillage());
        user.setTaluk(locationData.getTaluk());
        user.setDistrict(locationData.getDistrict());
        user.setState(locationData.getState());
        user.setPincode(locationData.getPincode());

        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(
                userRepository.findByUsernameContainingOrEmailContainingOrMobileNumberContaining(query, query, query));
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<Void> updatePreferences(
            @PathVariable Long id,
            @RequestBody UpdatePreferencesRequest request) {
        authService.updatePreferences(id, request.getPreferences());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/preferences")
    public ResponseEntity<Map<String, String>> getPreferences(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getPreferences(id));
    }
}
