package com.antigravity.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private LocationSection village;
    private LocationSection taluk;
    private LocationSection district;
    private LocationSection state;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationSection {
        private String name;
        private List<NewsArticle> news;
    }
}
