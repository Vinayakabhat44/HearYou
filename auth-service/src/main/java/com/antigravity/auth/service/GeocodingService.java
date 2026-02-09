package com.antigravity.auth.service;

import com.antigravity.auth.dto.LocationData;

public interface GeocodingService {
    LocationData reverseGeocode(double latitude, double longitude);
}
