package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMemberIdentityRequest {
    @NotBlank(message = "家庭身份不能为空")
    @Pattern(regexp = "FATHER|MOTHER|CHILD|OTHER", message = "家庭身份不合法")
    private String familyIdentity;
}
