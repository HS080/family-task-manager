package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态最小为0")
    @Max(value = 2, message = "状态最大为2")
    private Integer status;
}
