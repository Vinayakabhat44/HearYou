package com.antigravity.news.service;

import com.antigravity.news.adapter.IngestionAdapter;
import com.antigravity.news.entity.NewsArticleEntity;
import com.antigravity.news.entity.NewsSource;
import com.antigravity.news.repository.NewsArticleRepository;
import com.antigravity.news.repository.NewsSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackgroundIngestionService {

    private final NewsSourceRepository sourceRepository;
    private final NewsArticleRepository articleRepository;
    private final List<IngestionAdapter> adapters;

    @Scheduled(fixedRateString = "${news.ingestion.rate-ms:300000}") // Default 5 minutes
    @Transactional
    public void runIngestion() {
        log.info("Starting background news ingestion...");
        List<NewsSource> sources = sourceRepository.findByActiveTrue();

        for (NewsSource source : sources) {
            try {
                IngestionAdapter adapter = adapters.stream()
                        .filter(a -> a.supports(source.getType()))
                        .findFirst()
                        .orElse(null);

                if (adapter != null) {
                    List<NewsArticleEntity> newArticles = adapter.ingest(source);
                    saveUniqueArticles(newArticles);

                    source.setLastIngestedAt(LocalDateTime.now());
                    sourceRepository.save(source);
                }
            } catch (Exception e) {
                log.error("Failed to ingest from source {}: {}", source.getName(), e.getMessage());
            }
        }
        log.info("Background ingestion completed.");
    }

    private void saveUniqueArticles(List<NewsArticleEntity> articles) {
        for (NewsArticleEntity article : articles) {
            if (articleRepository.findByContentHash(article.getContentHash()).isEmpty()) {
                articleRepository.save(article);
            }
        }
    }
}
