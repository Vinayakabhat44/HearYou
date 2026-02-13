package com.antigravity.news.repository;

import com.antigravity.news.entity.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsArticleRepository extends JpaRepository<NewsArticleEntity, Long> {

    Optional<NewsArticleEntity> findByContentHash(String contentHash);

    List<NewsArticleEntity> findByPincodeOrderByIngestedAtDesc(String pincode);

    List<NewsArticleEntity> findByTalukOrderByIngestedAtDesc(String taluk);

    List<NewsArticleEntity> findByDistrictOrderByIngestedAtDesc(String district);

    List<NewsArticleEntity> findByStateOrderByIngestedAtDesc(String state);

    List<NewsArticleEntity> findByCategoryOrderByIngestedAtDesc(String category);

    @Query("SELECT n FROM NewsArticleEntity n WHERE n.country = :country AND (n.category = 'National' OR n.category = 'General') ORDER BY n.ingestedAt DESC")
    List<NewsArticleEntity> findNationalNews(@Param("country") String country);

    @Query("SELECT n FROM NewsArticleEntity n WHERE n.category = 'International' OR n.country != :country ORDER BY n.ingestedAt DESC")
    List<NewsArticleEntity> findInternationalNews(@Param("country") String country);
}
