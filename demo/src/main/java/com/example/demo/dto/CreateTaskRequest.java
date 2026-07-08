package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "任务名称不能为空")
    private String title;

    private String description;

    @NotNull(message = "负责人不能为空")
    private Long assigneeUserId;

    @NotNull(message = "截止时间不能为空")
    private LocalDateTime deadline;

    @Min(value = 0, message = "优先级最小为0")
    @Max(value = 2, message = "优先级最大为2")
    private Integer priority = 1;
}
