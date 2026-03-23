package com.mitraai.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticle {
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String source_id;
    private String image_url;
}
