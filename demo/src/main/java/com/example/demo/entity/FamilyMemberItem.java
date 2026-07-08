package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FamilyMemberItem {
    private Long userId;
    private String nickname;
    private String familyIdentity;
    private String roleCode;
    private LocalDateTime joinedAt;
}
