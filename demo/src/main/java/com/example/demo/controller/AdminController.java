package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.config.AuthContext;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserMapper userMapper;

    public AdminController(AdminService adminService, UserMapper userMapper) {
        this.adminService = adminService;
        this.userMapper = userMapper;
    }

    private User currentUser() {
        Long userId = AuthContext.getUserId();
        return userId == null ? null : userMapper.findById(userId);
    }

    @GetMapping("/stats")
    public ApiResponse<?> stats() {
        User me = currentUser();
        adminService.requireAdmin(me);
        return ApiResponse.success(adminService.stats());
    }

    @GetMapping("/users")
    public ApiResponse<?> users(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        User me = currentUser();
        adminService.requireAdmin(me);
        return ApiResponse.success(adminService.users(page, size));
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<?> updateUserStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> body) {
        User me = currentUser();
        adminService.requireAdmin(me);
        adminService.updateUserStatus(id, body.get("status"));
        return ApiResponse.success("用户状态已更新", true);
    }

    @GetMapping("/families")
    public ApiResponse<?> families(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        User me = currentUser();
        adminService.requireAdmin(me);
        return ApiResponse.success(adminService.families(page, size));
    }

    @GetMapping("/charts")
    public ApiResponse<?> charts() {
        User me = currentUser();
        adminService.requireAdmin(me);
        return ApiResponse.success(adminService.charts());
    }
}
