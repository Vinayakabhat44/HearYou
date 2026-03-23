package com.mitraai.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private String mobileNumber;
    private String village;
    private String taluk;
    private String district;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private Map<String, String> preferences;
}
