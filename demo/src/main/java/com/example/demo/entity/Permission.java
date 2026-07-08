package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Permission {
    private Long id;
    private Long familyId;
    private Long userId;
    private String roleCode;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
