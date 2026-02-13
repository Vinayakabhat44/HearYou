package com.antigravity.news.adapter;

import com.antigravity.news.entity.NewsArticleEntity;
import com.antigravity.news.entity.NewsSource;
import java.util.List;

public interface IngestionAdapter {
    List<NewsArticleEntity> ingest(NewsSource source);

    boolean supports(String type);
}
