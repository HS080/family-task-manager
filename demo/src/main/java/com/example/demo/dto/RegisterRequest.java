package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "家庭身份不能为空")
    @Pattern(regexp = "FATHER|MOTHER|CHILD|OTHER", message = "家庭身份不合法")
    private String familyIdentity;

    @NotBlank(message = "入局方式不能为空")
    @Pattern(regexp = "CREATE|JOIN", message = "入局方式只能为CREATE或JOIN")
    private String entryMode;

    private String familyName;
    private String inviteCode;
}
