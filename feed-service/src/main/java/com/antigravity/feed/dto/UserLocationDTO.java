package com.antigravity.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationDTO {
    private Long id;
    private String village;
    private String taluk;
    private String district;
    private String state;

    @JsonProperty("homeLocation")
    private Map<String, Double> homeLocation;

    public Double getLatitude() {
        return (homeLocation != null) ? homeLocation.get("latitude") : null;
    }

    public Double getLongitude() {
        return (homeLocation != null) ? homeLocation.get("longitude") : null;
    }
}
