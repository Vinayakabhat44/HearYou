package com.mitraai.core.news.adapter;

import com.mitraai.core.news.entity.NewsArticleEntity;
import com.mitraai.core.news.entity.NewsSource;
import java.util.List;

public interface IngestionAdapter {
    List<NewsArticleEntity> ingest(NewsSource source);

    boolean supports(String type);
}
