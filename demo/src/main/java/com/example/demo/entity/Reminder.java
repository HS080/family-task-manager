package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reminder {
    private Long id;
    private Long familyId;
    private Long userId;
    private Long taskId;
    private String remindType;
    private String content;
    private LocalDateTime remindAt;
    private Integer isRead;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
