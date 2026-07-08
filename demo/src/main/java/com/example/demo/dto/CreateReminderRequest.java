package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReminderRequest {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "提醒接收人不能为空")
    private Long userId;

    @NotNull(message = "提醒时间不能为空")
    private LocalDateTime remindAt;

    @NotBlank(message = "提醒内容不能为空")
    private String content;
}
