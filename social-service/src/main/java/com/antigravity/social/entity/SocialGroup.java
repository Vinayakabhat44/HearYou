package com.antigravity.social.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_groups")
@Data
@NoArgsConstructor
public class SocialGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();
}
