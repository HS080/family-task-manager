package com.example.demo.dto;

import com.example.demo.entity.FamilyGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminFamilyPageResponse {
    private long total;
    private int page;
    private int size;
    private List<FamilyGroup> list;
}
