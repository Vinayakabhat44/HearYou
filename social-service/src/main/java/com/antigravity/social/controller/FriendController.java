package com.antigravity.social.controller;

import com.antigravity.social.entity.Friendship;
import com.antigravity.social.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/social/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendshipRepository friendshipRepository;

    // TODO: retrieving current user ID from JWT header would be better, passing as
    // param for now
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam Long requesterId, @RequestBody FriendRequest request) {
        if (friendshipRepository.findByRequesterIdAndAddresseeId(requesterId, request.getTargetUserId()).isPresent()) {
            return ResponseEntity.badRequest().body("Request already exists");
        }

        Friendship friendship = new Friendship();
        friendship.setRequesterId(requesterId);
        friendship.setAddresseeId(request.getTargetUserId());
        friendship.setStatus(Friendship.Status.PENDING);
        return ResponseEntity.ok(friendshipRepository.save(friendship));
    }

    @PutMapping("/{requestId}/respond")
    public ResponseEntity<?> respondToRequest(@PathVariable Long requestId, @RequestParam String status) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        friendship.setStatus(Friendship.Status.valueOf(status.toUpperCase()));
        friendship.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(friendshipRepository.save(friendship));
    }

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<Long>> getFriendIds(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllAcceptedFriendships(userId);
        List<Long> friendIds = new ArrayList<>();
        for (Friendship f : friendships) {
            if (f.getRequesterId().equals(userId)) {
                friendIds.add(f.getAddresseeId());
            } else {
                friendIds.add(f.getRequesterId());
            }
        }
        return ResponseEntity.ok(friendIds);
    }

    @GetMapping("/{userId}/pending")
    public ResponseEntity<List<Friendship>> getPendingRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipRepository.findByAddresseeIdAndStatus(userId, Friendship.Status.PENDING));
    }
}
