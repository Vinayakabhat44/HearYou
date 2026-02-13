package com.antigravity.news.adapter.impl;

import com.antigravity.news.adapter.IngestionAdapter;
import com.antigravity.news.entity.NewsArticleEntity;
import com.antigravity.news.entity.NewsSource;
import com.antigravity.news.service.LocationResolver;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

@Component
@Slf4j
public class RssIngestionAdapter implements IngestionAdapter {

    private final LocationResolver locationResolver;

    public RssIngestionAdapter(LocationResolver locationResolver) {
        this.locationResolver = locationResolver;
    }

    @Override
    public List<NewsArticleEntity> ingest(NewsSource source) {
        List<NewsArticleEntity> articles = new ArrayList<>();
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(source.getUrl())));

            for (SyndEntry entry : feed.getEntries()) {
                String title = entry.getTitle();
                String link = entry.getLink();
                String content = entry.getDescription() != null ? entry.getDescription().getValue() : "";

                // Extract categories
                String rawCategory = "";
                if (!entry.getCategories().isEmpty()) {
                    rawCategory = entry.getCategories().get(0).getName();
                }

                NewsArticleEntity article = NewsArticleEntity.builder()
                        .title(title)
                        .link(link)
                        .content(content)
                        .summary(content.length() > 200 ? content.substring(0, 200) + "..." : content)
                        .source(source.getName())
                        .pubDate(entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "")
                        .language(source.getLanguage())
                        .category(rawCategory) // Set initial category from RSS
                        .ingestedAt(LocalDateTime.now())
                        .contentHash(generateHash(title + link))
                        .build();

                locationResolver.resolveAndTag(article);
                articles.add(article);
            }
        } catch (Exception e) {
            log.error("Error ingesting RSS from {}: {}", source.getUrl(), e.getMessage());
        }
        return articles;
    }

    @Override
    public boolean supports(String type) {
        return "RSS".equalsIgnoreCase(type);
    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
