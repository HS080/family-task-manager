package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String userUid;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    // 用户角色：USER / ADMIN
    private String role;
    // 家庭身份（爸爸/妈妈/孩子/其他），复用 avatar_url 字段存储
    private String familyIdentity;
    private Long familyId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
