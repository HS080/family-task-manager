package com.example.demo.entity;

import lombok.Data;

@Data
public class MemberTaskStat {
    private Long userId;
    private String nickname;
    private Integer completedCount;
}
