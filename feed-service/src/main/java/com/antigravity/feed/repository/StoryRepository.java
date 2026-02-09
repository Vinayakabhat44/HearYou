package com.antigravity.feed.repository;

import com.antigravity.feed.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    @Query(value = "SELECT * FROM stories WHERE ST_Distance_Sphere(location, ST_GeomFromText(:point, 4326)) <= :radiusMeters ORDER BY created_at DESC", nativeQuery = true)
    List<Story> findStoriesNearLocation(@Param("point") String point, @Param("radiusMeters") double radiusMeters);

    @Query("SELECT s FROM Story s WHERE s.village = :village OR s.taluk = :taluk OR s.district = :district " +
            "ORDER BY CASE " +
            "  WHEN s.village = :village THEN 1 " +
            "  WHEN s.taluk = :taluk THEN 2 " +
            "  WHEN s.district = :district THEN 3 " +
            "  ELSE 4 END ASC, s.createdAt DESC")
    Page<Story> findHierarchical(@Param("village") String village,
            @Param("taluk") String taluk,
            @Param("district") String district,
            Pageable pageable);

    List<Story> findByUserIdOrderByCreatedAtDesc(Long userId);
}
