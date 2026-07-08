package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Long id;
    private Long familyId;
    private String title;
    private String description;
    private Long assigneeUserId;
    private Long creatorUserId;
    private Integer status;
    private Integer priority;
    private LocalDateTime deadline;
    private LocalDateTime completedAt;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
