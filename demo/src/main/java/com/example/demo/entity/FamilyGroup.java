package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FamilyGroup {
    private Long id;
    private String familyName;
    private String inviteCode;
    private Long ownerUserId;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
