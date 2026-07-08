package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String userUid;
    private String nickname;
    private String role;
    private Long familyId;
}
