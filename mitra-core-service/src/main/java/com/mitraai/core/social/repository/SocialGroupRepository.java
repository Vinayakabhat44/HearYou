package com.mitraai.core.social.repository;

import com.mitraai.core.social.entity.SocialGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialGroupRepository extends JpaRepository<SocialGroup, Long> {
    List<SocialGroup> findByCreatedBy(Long userId);
}
