package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFamilyNameRequest {
    @NotBlank(message = "家庭名称不能为空")
    private String familyName;
}
