package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinFamilyByCodeRequest {

    @NotBlank(message = "家庭邀请码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "家庭邀请码格式不正确")
    private String inviteCode;
}
