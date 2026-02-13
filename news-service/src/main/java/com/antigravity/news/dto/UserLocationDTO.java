package com.antigravity.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationDTO {
    private Long id;
    private String username;
    private String village;
    private String taluk;
    private String district;
    private String state;
}
