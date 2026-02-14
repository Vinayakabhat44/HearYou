package com.antigravity.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String mobileNumber;

    // Optional location fields
    private String village;
    private String taluk;
    private String district;
    private String state;
    private String pincode;

    // Optional coordinates for automatic geocoding
    private Double latitude;
    private Double longitude;
}
