package com.antigravity.feed.service;

import com.antigravity.feed.dto.LocationData;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Primary
public class OpenStreetMapService implements GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOMINATIM_API = "https://nominatim.openstreetmap.org/reverse?format=json&lat={lat}&lon={lon}";

    @Override
    public LocationData reverseGeocode(double latitude, double longitude) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "Antigravity-Feed-Service/1.0"); // Nominatim requires User-Agent
            HttpEntity<String> entity = new HttpEntity<>(headers);

            Map<String, Object> params = Map.of(
                    "lat", latitude,
                    "lon", longitude);

            ResponseEntity<Map> response = restTemplate.exchange(
                    NOMINATIM_API,
                    HttpMethod.GET,
                    entity,
                    Map.class,
                    params);

            if (response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.containsKey("address")) {
                    Map<String, String> address = (Map<String, String>) body.get("address");

                    String village = address.getOrDefault("village",
                            address.getOrDefault("hamlet",
                                    address.getOrDefault("suburb",
                                            address.getOrDefault("town", ""))));

                    String taluk = address.getOrDefault("county",
                            address.getOrDefault("city",
                                    address.getOrDefault("municipality", "")));

                    String district = address.getOrDefault("state_district",
                            address.getOrDefault("district", ""));

                    String state = address.getOrDefault("state", "");

                    return new LocationData(village, taluk, district, state);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error but don't crash
        }

        // Fallback or empty if failed
        return new LocationData("", "", "", "");
    }
}
