package com.antigravity.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedFeedResponse {

    private LocationInfo userLocation;
    private FeedContent feed;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationInfo {
        private String pincode;
        private String taluk;
        private String district;
        private String state;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedContent {
        private List<NewsArticle> pincode;
        private List<NewsArticle> taluk;
        private List<NewsArticle> district;
        private List<NewsArticle> state;
        private List<NewsArticle> national;
        private List<NewsArticle> international;
        private Map<String, List<NewsArticle>> categories;
    }
}
