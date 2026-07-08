package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskCommentRequest {
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
