package com.mitraai.core.feed.service;

import com.mitraai.core.dto.LocationData;

public interface GeocodingService {
    LocationData reverseGeocode(double latitude, double longitude);
}
