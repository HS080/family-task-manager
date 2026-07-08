package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskActivity {
    private Long id;
    private Long taskId;
    private Long operatorUserId;
    private String actionType;
    private String actionDesc;
    private String operatorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
