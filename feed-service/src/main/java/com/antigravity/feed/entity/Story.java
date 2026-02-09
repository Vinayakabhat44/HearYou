package com.antigravity.feed.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
@Data
@NoArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simple storage for now, assuming S3 URL or similar
    private String mediaUrl;

    @Lob
    private String textContent;

    @Enumerated(EnumType.STRING)
    private StoryType type;

    // Store userId instead of full User entity (microservice separation)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "POINT")
    private Point location;

    private String village;
    private String taluk;
    private String district;
    private String state;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum StoryType {
        TEXT, AUDIO, VIDEO
    }
}
