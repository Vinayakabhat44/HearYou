package com.antigravity.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationData {
    private String village;
    private String taluk;
    private String district;
    private String state;
    private String pincode;
}
