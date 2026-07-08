package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateFamilyRequest {
    private String familyName;
    private Long creatorUserId;
}
