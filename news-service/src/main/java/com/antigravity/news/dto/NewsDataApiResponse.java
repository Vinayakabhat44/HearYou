package com.antigravity.news.dto;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDataApiResponse {
    private String status;
    private int totalResults;
    private List<NewsArticle> results;
}
