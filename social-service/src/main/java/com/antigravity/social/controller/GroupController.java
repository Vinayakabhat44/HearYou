package com.antigravity.social.controller;

import com.antigravity.social.entity.GroupMember;
import com.antigravity.social.entity.SocialGroup;
import com.antigravity.social.repository.GroupMemberRepository;
import com.antigravity.social.repository.SocialGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/social/groups")
@RequiredArgsConstructor
public class GroupController {

    private final SocialGroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        SocialGroup group = new SocialGroup();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(request.getCreatedBy());
        group = groupRepository.save(group);

        // Add creator as ADMIN
        GroupMember member = new GroupMember();
        member.setSocialGroup(group);
        member.setUserId(request.getCreatedBy());
        member.setRole(GroupMember.Role.ADMIN);
        memberRepository.save(member);

        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long groupId, @RequestBody AddMemberRequest request) {
        SocialGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (memberRepository.findBySocialGroupIdAndUserId(groupId, request.getUserId()).isPresent()) {
            return ResponseEntity.badRequest().body("User already in group");
        }

        GroupMember member = new GroupMember();
        member.setSocialGroup(group);
        member.setUserId(request.getUserId());
        member.setRole(GroupMember.Role.MEMBER);
        memberRepository.save(member);

        return ResponseEntity.ok(member);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> listMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(memberRepository.findBySocialGroupId(groupId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SocialGroup>> listUserGroups(@PathVariable Long userId) {
        List<GroupMember> memberships = memberRepository.findByUserId(userId);
        List<SocialGroup> groups = memberships.stream()
                .map(GroupMember::getSocialGroup)
                .collect(Collectors.toList());
        return ResponseEntity.ok(groups);
    }
}
