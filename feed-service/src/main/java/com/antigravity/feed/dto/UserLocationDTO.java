package com.antigravity.feed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLocationDTO {
    private Long id;
    private String village;
    private String taluk;
    private String district;
    private String state;

    @JsonProperty("homeLocation")
    private Map<String, Double> homeLocation;

    // Handle legacy/root-level latitude during deserialization
    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        if (latitude != null) {
            if (this.homeLocation == null)
                this.homeLocation = new HashMap<>();
            this.homeLocation.put("latitude", latitude);
        }
    }

    // Handle legacy/root-level longitude during deserialization
    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        if (longitude != null) {
            if (this.homeLocation == null)
                this.homeLocation = new HashMap<>();
            this.homeLocation.put("longitude", longitude);
        }
    }

    public Double getLatitude() {
        return (homeLocation != null) ? homeLocation.get("latitude") : null;
    }

    public Double getLongitude() {
        return (homeLocation != null) ? homeLocation.get("longitude") : null;
    }
}
