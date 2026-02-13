package com.antigravity.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_sources")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String url;

    private String type; // RSS, API

    private String language;

    private String region; // State or District

    @Builder.Default
    private boolean active = true;

    private LocalDateTime lastIngestedAt;

    private Integer ingestionFrequencyMinutes;
}
