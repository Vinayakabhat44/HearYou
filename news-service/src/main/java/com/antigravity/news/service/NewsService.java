package com.antigravity.news.service;

import com.antigravity.news.dto.NewsArticle;
import com.antigravity.news.dto.NewsDataApiResponse;
import com.antigravity.news.dto.NewsResponse;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private final RestTemplate restTemplate;

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.base-url}")
    private String baseUrl;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NewsResponse getLocalNews(String village, String taluk, String district, String state, String language) {
        String lang = (language == null || language.isEmpty()) ? "en" : language;
        return NewsResponse.builder()
                .village(fetchNewsForLocation(village, "Village", lang))
                .taluk(fetchNewsForLocation(taluk, "Taluk", lang))
                .district(fetchNewsForLocation(district, "District", lang))
                .state(fetchNewsForLocation(state, "State", lang))
                .build();
    }

    private NewsResponse.LocationSection fetchNewsForLocation(String locationName, String level, String language) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return NewsResponse.LocationSection.builder()
                    .name("Unknown " + level)
                    .news(new ArrayList<>())
                    .build();
        }

        List<NewsArticle> news = getNewsForKeyword(locationName.trim(), language);

        return NewsResponse.LocationSection.builder()
                .name(locationName)
                .news(news)
                .build();
    }

    @Cacheable(value = "newsCache", key = "#keyword + #language")
    public List<NewsArticle> getNewsForKeyword(String keyword, String language) {
        List<NewsArticle> allNews = new ArrayList<>();

        // 1. Fetch from NewsData.io (if API key present)
        allNews.addAll(fetchFromNewsData(keyword, language));

        // 2. Supplement with Google News RSS (Great for local keywords)
        allNews.addAll(fetchFromGoogleNewsRss(keyword, language));

        // De-duplicate by title (basic)
        return allNews.stream()
                .filter(distinctByKey(NewsArticle::getTitle))
                .limit(20) // Limit to top 20 results per section
                .collect(Collectors.toList());
    }

    private List<NewsArticle> fetchFromNewsData(String keyword, String language) {
        if (apiKey == null || apiKey.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("apikey", apiKey)
                    .queryParam("q", "\"" + keyword + "\"") // Use quotes for exact local match
                    .queryParam("country", "in")
                    .queryParam("language", language)
                    .toUriString();

            NewsDataApiResponse response = restTemplate.getForObject(url, NewsDataApiResponse.class);
            if (response != null && "success".equals(response.getStatus()) && response.getResults() != null) {
                return response.getResults();
            }
        } catch (Exception e) {
            log.error("Error fetching from NewsData.io for: {}", keyword, e);
        }
        return new ArrayList<>();
    }

    private List<NewsArticle> fetchFromGoogleNewsRss(String keyword, String language) {
        try {
            // Google News RSS URL for India
            String query = keyword + " news";
            String rssUrl = String.format("https://news.google.com/rss/search?q=%s&hl=%s-IN&gl=IN&ceid=IN:%s",
                    query.replace(" ", "+"),
                    language,
                    language);

            log.info("Fetching Google News RSS for: {}", rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(rssUrl)));

            return feed.getEntries().stream()
                    .map(entry -> NewsArticle.builder()
                            .title(entry.getTitle())
                            .link(entry.getLink())
                            .description(entry.getDescription() != null ? entry.getDescription().getValue() : "")
                            .pubDate(entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "")
                            .source_id("Google News")
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching from Google News RSS for: {}", keyword, e);
        }
        return new ArrayList<>();
    }

    private static <T> java.util.function.Predicate<T> distinctByKey(
            java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
