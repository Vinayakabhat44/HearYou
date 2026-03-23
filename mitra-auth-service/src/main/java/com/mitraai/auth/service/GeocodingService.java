package com.mitraai.auth.service;

import com.mitraai.auth.dto.LocationData;

public interface GeocodingService {
    LocationData reverseGeocode(double latitude, double longitude);
}
