package com.antigravity.news.config;

import com.antigravity.news.entity.NewsSource;
import com.antigravity.news.repository.NewsSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final NewsSourceRepository sourceRepository;

    @Override
    public void run(String... args) {
        if (sourceRepository.count() == 0) {
            sourceRepository.saveAll(List.of(
                    NewsSource.builder()
                            .name("The Hindu - Karnataka")
                            .url("https://www.thehindu.com/news/national/karnataka/feeder/default.rss")
                            .type("RSS")
                            .language("en")
                            .region("Karnataka")
                            .build(),
                    NewsSource.builder()
                            .name("Prajavani - Bangalore")
                            .url("https://www.prajavani.net/bangalore/rss.xml")
                            .type("RSS")
                            .language("kn")
                            .region("Bangalore")
                            .build(),
                    NewsSource.builder()
                            .name("Money Control - Finance")
                            .url("https://www.moneycontrol.com/rss/MC_news.xml")
                            .type("RSS")
                            .language("en")
                            .build()));
        }
    }
}
