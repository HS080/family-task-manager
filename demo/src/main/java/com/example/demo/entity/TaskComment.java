package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskComment {
    private Long id;
    private Long taskId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
