package com.mitraai.core.news.config;

import com.mitraai.core.news.entity.NewsSource;
import com.mitraai.core.news.repository.NewsSourceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final NewsSourceRepository sourceRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        try {
            InputStream inputStream = new ClassPathResource("sources.json").getInputStream();
            List<NewsSource> sources = objectMapper.readValue(inputStream,
                    new TypeReference<List<NewsSource>>() {
                    });
            
            int addedCount = 0;
            for (NewsSource source : sources) {
                if (!sourceRepository.existsByUrl(source.getUrl())) {
                    sourceRepository.save(source);
                    addedCount++;
                }
            }
            if (addedCount > 0) {
                log.info("Successfully added {} new news sources from JSON.", addedCount);
            }
        } catch (Exception e) {
            log.error("Failed to seed news sources: {}", e.getMessage(), e);
        }
    }
}
