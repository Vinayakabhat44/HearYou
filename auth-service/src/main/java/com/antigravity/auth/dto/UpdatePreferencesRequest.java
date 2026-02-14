package com.antigravity.auth.dto;

import lombok.Data;
import java.util.Map;

@Data
public class UpdatePreferencesRequest {
    private Map<String, String> preferences;
}
