package com.mitraai.core.news.service;

import com.mitraai.core.dto.LocalizedFeedResponse;
import com.mitraai.core.dto.NewsArticle;
import com.mitraai.core.dto.UserLocationDTO;
import com.mitraai.core.news.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final NewsArticleRepository articleRepository;
    private final LocationContextService locationContextService;

    public LocalizedFeedResponse getLocalFeed(String pincode, String taluk, String district, String state) {
        // Resolve location: Use provided params OR fallback to context (token-based)
        if (pincode == null && taluk == null && district == null) {
            UserLocationDTO userLocation = locationContextService.getCurrentUserLocation();
            if (userLocation != null) {
                pincode = "Unknown"; // Pincode might not be in UserLocationDTO yet
                taluk = userLocation.getTaluk();
                district = userLocation.getDistrict();
                state = userLocation.getState();
            }
        }

        LocalizedFeedResponse.LocationInfo locationInfo = LocalizedFeedResponse.LocationInfo.builder()
                .pincode(pincode)
                .taluk(taluk)
                .district(district)
                .state(state)
                .build();

        return LocalizedFeedResponse.builder()
                .userLocation(locationInfo)
                .feed(LocalizedFeedResponse.FeedContent.builder()
                        .pincode(mapToDto(articleRepository.findByPincodeOrderByIngestedAtDesc(pincode)))
                        .taluk(mapToDto(articleRepository.findByTalukIgnoreCaseOrderByIngestedAtDesc(taluk)))
                        .district(mapToDto(articleRepository.findByDistrictIgnoreCaseOrderByIngestedAtDesc(district)))
                        .state(mapToDto(articleRepository.findByStateIgnoreCaseOrderByIngestedAtDesc(state)))
                        .national(mapToDto(articleRepository.findNationalNews("India")))
                        .international(mapToDto(articleRepository.findInternationalNews("India")))
                        .categories(fetchCategories())
                        .build())
                .build();
    }

    private Map<String, List<NewsArticle>> fetchCategories() {
        Map<String, List<NewsArticle>> categories = new HashMap<>();
        categories.put("cricket", mapToDto(articleRepository.findByCategoryIgnoreCaseOrderByIngestedAtDesc("Cricket")));
        categories.put("finance", mapToDto(articleRepository.findByCategoryIgnoreCaseOrderByIngestedAtDesc("Finance")));
        categories.put("health", mapToDto(articleRepository.findByCategoryIgnoreCaseOrderByIngestedAtDesc("Health")));
        categories.put("entertainment",
                mapToDto(articleRepository.findByCategoryIgnoreCaseOrderByIngestedAtDesc("Entertainment")));
        categories.put("sports", mapToDto(articleRepository.findByCategoryIgnoreCaseOrderByIngestedAtDesc("Sports")));
        return categories;
    }

    private List<NewsArticle> mapToDto(List<com.mitraai.core.news.entity.NewsArticleEntity> entities) {
        return entities.stream().limit(10).map(entity -> NewsArticle.builder()
                .title(entity.getTitle())
                .link(entity.getLink())
                .description(entity.getSummary())
                .pubDate(entity.getPubDate())
                .source_id(entity.getSource())
                .image_url(entity.getImageUrl())
                .build()).collect(Collectors.toList());
    }

    // Keep the old method for backward compatibility if needed, but refactor to use
    // DB
    @Cacheable(value = "newsCache", key = "#keyword + #language")
    public List<NewsArticle> getNewsForKeyword(String keyword, String language) {
        // Basic fallback to repo search if keyword matches any location entity
        return mapToDto(articleRepository.findByDistrictIgnoreCaseOrderByIngestedAtDesc(keyword));
    }
}
