package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.CreateReminderRequest;
import com.example.demo.entity.Reminder;
import com.example.demo.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ApiResponse<Reminder> create(@RequestBody @Valid CreateReminderRequest request) {
        return ApiResponse.success("提醒创建成功", reminderService.create(request));
    }

    @GetMapping("/me")
    public ApiResponse<List<Reminder>> myReminders() {
        return ApiResponse.success(reminderService.myReminders());
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Boolean> markRead(@PathVariable Long id) {
        return ApiResponse.success("已标记已读", reminderService.markRead(id));
    }

    @PatchMapping("/{id}/close")
    public ApiResponse<Boolean> close(@PathVariable Long id) {
        return ApiResponse.success("已关闭提醒", reminderService.close(id));
    }
}
