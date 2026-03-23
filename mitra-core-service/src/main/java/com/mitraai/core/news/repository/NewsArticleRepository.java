package com.mitraai.core.news.repository;

import com.mitraai.core.news.entity.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsArticleRepository extends JpaRepository<NewsArticleEntity, Long> {

    Optional<NewsArticleEntity> findByContentHash(String contentHash);

    List<NewsArticleEntity> findByPincodeOrderByIngestedAtDesc(String pincode);

    List<NewsArticleEntity> findByTalukIgnoreCaseOrderByIngestedAtDesc(String taluk);

    List<NewsArticleEntity> findByDistrictIgnoreCaseOrderByIngestedAtDesc(String district);

    List<NewsArticleEntity> findByStateIgnoreCaseOrderByIngestedAtDesc(String state);

    List<NewsArticleEntity> findByCategoryIgnoreCaseOrderByIngestedAtDesc(String category);

    @Query("SELECT n FROM NewsArticleEntity n WHERE LOWER(n.country) = LOWER(:country) AND (LOWER(n.category) = 'national' OR LOWER(n.category) = 'general') ORDER BY n.ingestedAt DESC")
    List<NewsArticleEntity> findNationalNews(@Param("country") String country);

    @Query("SELECT n FROM NewsArticleEntity n WHERE LOWER(n.category) = 'international' OR LOWER(n.country) != LOWER(:country) ORDER BY n.ingestedAt DESC")
    List<NewsArticleEntity> findInternationalNews(@Param("country") String country);
}
