package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminUserPageResponse {
    private long total;
    private int page;
    private int size;
    private List<User> list;
}
