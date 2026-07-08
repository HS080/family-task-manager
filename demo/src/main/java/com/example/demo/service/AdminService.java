package com.example.demo.service;

import com.example.demo.dto.AdminChartResponse;
import com.example.demo.dto.AdminFamilyPageResponse;
import com.example.demo.dto.AdminStatsResponse;
import com.example.demo.dto.AdminUserPageResponse;
import com.example.demo.entity.FamilyGroup;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.FamilyGroupMapper;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserMapper userMapper;
    private final FamilyGroupMapper familyGroupMapper;
    private final TaskMapper taskMapper;

    public AdminService(UserMapper userMapper, FamilyGroupMapper familyGroupMapper, TaskMapper taskMapper) {
        this.userMapper = userMapper;
        this.familyGroupMapper = familyGroupMapper;
        this.taskMapper = taskMapper;
    }

    public void requireAdmin(User currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(401, "未登录或登录已过期");
        }
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new BusinessException(403, "仅管理员可访问");
        }
    }

    public AdminStatsResponse stats() {
        long userCount = userMapper.countAll();
        long familyCount = familyGroupMapper.countAll();
        long taskCount = taskMapper.countAll();
        return new AdminStatsResponse(userCount, familyCount, taskCount);
    }

    public AdminUserPageResponse users(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);
        int offset = (safePage - 1) * safeSize;

        long total = userMapper.countAll();
        List<User> list = userMapper.pageAll(offset, safeSize);
        return new AdminUserPageResponse(total, safePage, safeSize, list);
    }

    public void updateUserStatus(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(400, "status 只能为 0 或 1");
        }
        int updated = userMapper.updateStatus(id, status);
        if (updated == 0) {
            throw new BusinessException(404, "用户不存在");
        }
    }

    public AdminFamilyPageResponse families(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);
        int offset = (safePage - 1) * safeSize;

        long total = familyGroupMapper.countAll();
        List<FamilyGroup> list = familyGroupMapper.pageAll(offset, safeSize);
        return new AdminFamilyPageResponse(total, safePage, safeSize, list);
    }

    public AdminChartResponse charts() {
        return new AdminChartResponse(
            List.of(
                new AdminChartResponse.ChartItem("管理员", userMapper.countAdmins()),
                new AdminChartResponse.ChartItem("普通用户", userMapper.countNormalUsers())
            ),
            List.of(
                new AdminChartResponse.ChartItem("正常", userMapper.countActiveUsers()),
                new AdminChartResponse.ChartItem("禁用", userMapper.countDisabledUsers())
            ),
            List.of(
                new AdminChartResponse.ChartItem("近7日-1", 18),
                new AdminChartResponse.ChartItem("近7日-2", 24),
                new AdminChartResponse.ChartItem("近7日-3", 15),
                new AdminChartResponse.ChartItem("近7日-4", 32),
                new AdminChartResponse.ChartItem("近7日-5", 28),
                new AdminChartResponse.ChartItem("近7日-6", 41),
                new AdminChartResponse.ChartItem("近7日-7", 36)
            )
        );
    }
}
