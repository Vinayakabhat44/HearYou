package com.mitraai.core.news.repository;

import com.mitraai.core.news.entity.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
    List<NewsSource> findByActiveTrue();

    boolean existsByUrl(String url);
}
