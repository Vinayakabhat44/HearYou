package com.antigravity.social.repository;

import com.antigravity.social.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findBySocialGroupId(Long groupId);

    List<GroupMember> findByUserId(Long userId);

    Optional<GroupMember> findBySocialGroupIdAndUserId(Long groupId, Long userId);
}
