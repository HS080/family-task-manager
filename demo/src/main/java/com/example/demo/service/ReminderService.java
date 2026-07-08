package com.example.demo.service;

import com.example.demo.config.AuthContext;
import com.example.demo.dto.CreateReminderRequest;
import com.example.demo.entity.Reminder;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.ReminderMapper;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    private final ReminderMapper reminderMapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final PermissionService permissionService;

    public ReminderService(ReminderMapper reminderMapper,
                           TaskMapper taskMapper,
                           UserMapper userMapper,
                           PermissionService permissionService) {
        this.reminderMapper = reminderMapper;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.permissionService = permissionService;
    }

    public Reminder create(CreateReminderRequest request) {
        Long currentUserId = AuthContext.getUserId();
        Task task = taskMapper.findById(request.getTaskId());
        if (task == null) {
            throw new BusinessException(404, "任务不存在");
        }
        permissionService.requirePermission(task.getFamilyId(), currentUserId);

        User receiver = userMapper.findById(request.getUserId());
        if (receiver == null || !task.getFamilyId().equals(receiver.getFamilyId())) {
            throw new BusinessException(400, "提醒接收人必须是同家庭成员");
        }

        Reminder reminder = new Reminder();
        reminder.setFamilyId(task.getFamilyId());
        reminder.setTaskId(task.getId());
        reminder.setUserId(request.getUserId());
        reminder.setRemindType("MANUAL");
        reminder.setContent(request.getContent().trim());
        reminder.setRemindAt(request.getRemindAt());
        reminderMapper.insert(reminder);
        return reminder;
    }

    public List<Reminder> myReminders() {
        Long currentUserId = AuthContext.getUserId();
        User me = userMapper.findById(currentUserId);
        if (me == null || me.getFamilyId() == null) {
            return java.util.Collections.emptyList();
        }
        return reminderMapper.listByUserId(currentUserId, me.getFamilyId());
    }

    public Boolean markRead(Long id) {
        Long currentUserId = AuthContext.getUserId();
        return reminderMapper.markRead(id, currentUserId) > 0;
    }

    public Boolean close(Long id) {
        Long currentUserId = AuthContext.getUserId();
        return reminderMapper.close(id, currentUserId) > 0;
    }
}
