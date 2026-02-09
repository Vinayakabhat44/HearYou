package com.antigravity.feed.service;

import com.antigravity.feed.dto.LocationData;

public interface GeocodingService {
    LocationData reverseGeocode(double latitude, double longitude);
}
