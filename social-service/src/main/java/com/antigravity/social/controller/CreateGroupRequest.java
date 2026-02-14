package com.antigravity.social.controller;

import lombok.Data;

@Data
public class CreateGroupRequest {
    private String name;
    private String description;
    private Long createdBy;
}
