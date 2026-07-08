package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success("登录成功", authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.success("注册成功", authService.register(request));
    }

    @PostMapping("/reset-password")
    public ApiResponse<Boolean> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return ApiResponse.success("密码重置成功", authService.resetPassword(request.getUsername(), request.getNewPassword()));
    }
}
