package com.antigravity.social.repository;

import com.antigravity.social.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByRequesterIdAndAddresseeId(Long requesterId, Long addresseeId);

    // Find all friendships involving a user where status is ACCEPTED
    @Query("SELECT f FROM Friendship f WHERE (f.requesterId = :userId OR f.addresseeId = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findAllAcceptedFriendships(Long userId);

    List<Friendship> findByAddresseeIdAndStatus(Long addresseeId, Friendship.Status status);
}
