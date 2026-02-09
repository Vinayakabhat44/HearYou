package com.antigravity.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String role = "USER";

    @Column(columnDefinition = "POINT")
    @JsonIgnore
    private Point homeLocation;

    private String village;
    private String taluk;
    private String district;
    private String state;
    private String pincode;

    @JsonProperty("homeLocation")
    public Map<String, Double> getFormattedLocation() {
        if (homeLocation == null) {
            return null;
        }
        Map<String, Double> coords = new HashMap<>();
        coords.put("longitude", homeLocation.getX());
        coords.put("latitude", homeLocation.getY());
        return coords;
    }
}
