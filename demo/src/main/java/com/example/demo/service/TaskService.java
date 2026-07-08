package com.example.demo.service;

import com.example.demo.config.AuthContext;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.UpdateTaskStatusRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final PermissionService permissionService;

    public TaskService(TaskMapper taskMapper, UserMapper userMapper, PermissionService permissionService) {
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.permissionService = permissionService;
    }

    public Task createTask(CreateTaskRequest request) {
        Long currentUserId = AuthContext.getUserId();
        User me = userMapper.findById(currentUserId);
        if (me == null || me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }

        permissionService.requirePermission(me.getFamilyId(), currentUserId);

        User assignee = userMapper.findById(request.getAssigneeUserId());
        if (assignee == null || !me.getFamilyId().equals(assignee.getFamilyId())) {
            throw new BusinessException(400, "负责人必须为当前家庭成员");
        }

        Task task = new Task();
        task.setFamilyId(me.getFamilyId());
        task.setTitle(request.getTitle().trim());
        task.setDescription(request.getDescription());
        task.setAssigneeUserId(request.getAssigneeUserId());
        task.setCreatorUserId(currentUserId);
        task.setStatus(0);
        task.setPriority(request.getPriority() == null ? 1 : request.getPriority());
        task.setDeadline(request.getDeadline());
        taskMapper.insert(task);
        return task;
    }

    public List<Task> listTasks(Boolean mineOnly) {
        Long currentUserId = AuthContext.getUserId();
        User me = userMapper.findById(currentUserId);
        if (me == null || me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        List<Task> tasks = taskMapper.listByFamilyId(me.getFamilyId());
        if (Boolean.TRUE.equals(mineOnly)) {
            return tasks.stream().filter(t -> currentUserId.equals(t.getAssigneeUserId())).toList();
        }
        return tasks;
    }

    public Task getTaskDetail(Long taskId) {
        Long currentUserId = AuthContext.getUserId();
        return getAndCheckFamilyTask(taskId, currentUserId);
    }

    public Task updateStatus(Long taskId, UpdateTaskStatusRequest request) {
        Long currentUserId = AuthContext.getUserId();
        getAndCheckFamilyTask(taskId, currentUserId);
        LocalDateTime completedAt = request.getStatus() == 2 ? LocalDateTime.now() : null;
        taskMapper.updateStatus(taskId, request.getStatus(), completedAt);
        return taskMapper.findById(taskId);
    }

    public void deleteTask(Long taskId) {
        Long currentUserId = AuthContext.getUserId();
        getAndCheckFamilyTask(taskId, currentUserId);
        taskMapper.softDelete(taskId);
    }

    private Task getAndCheckFamilyTask(Long taskId, Long currentUserId) {
        Task task = taskMapper.findById(taskId);
        if (task == null) {
            throw new BusinessException(404, "任务不存在");
        }
        permissionService.requirePermission(task.getFamilyId(), currentUserId);
        return task;
    }
}
