package com.antigravity.news.config;

import com.antigravity.news.entity.NewsSource;
import com.antigravity.news.repository.NewsSourceRepository;
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
                if (sourceRepository.count() == 0) {
                        try {
                                InputStream inputStream = new ClassPathResource("sources.json").getInputStream();
                                List<NewsSource> sources = objectMapper.readValue(inputStream,
                                                new TypeReference<List<NewsSource>>() {
                                                });
                                sourceRepository.saveAll(sources);
                                log.info("Successfully seeded {} news sources from JSON.", sources.size());
                        } catch (Exception e) {
                                log.error("Failed to seed news sources: {}", e.getMessage(), e);
                        }
                }
        }
}
