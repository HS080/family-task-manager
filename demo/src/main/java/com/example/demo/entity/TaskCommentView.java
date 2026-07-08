package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCommentView {
    private Long id;
    private Long taskId;
    private Long userId;
    private String content;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
