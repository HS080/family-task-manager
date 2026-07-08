package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMemberRoleRequest {
    @NotBlank(message = "角色不能为空")
    private String roleCode;
}
