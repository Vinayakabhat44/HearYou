package com.antigravity.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_articles", indexes = {
        @Index(name = "idx_pincode", columnList = "pincode"),
        @Index(name = "idx_taluk", columnList = "taluk"),
        @Index(name = "idx_district", columnList = "district"),
        @Index(name = "idx_state", columnList = "state"),
        @Index(name = "idx_category", columnList = "category")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    private String summary;

    private String imageUrl;

    private String source;

    private String pubDate; // Keep as string for now if diverse formats

    private LocalDateTime ingestedAt;

    private String language;

    private String category; // Cricket, Finance, Health, etc.

    // Location Fields
    private String pincode;
    private String taluk;
    private String district;
    private String state;
    private String country;

    @Column(unique = true)
    private String contentHash;
}
