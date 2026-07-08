package com.example.demo.service;

import com.example.demo.config.AuthContext;
import com.example.demo.entity.MemberTaskStat;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.StatisticsMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final UserMapper userMapper;
    private final PermissionService permissionService;

    public StatisticsService(StatisticsMapper statisticsMapper, UserMapper userMapper, PermissionService permissionService) {
        this.statisticsMapper = statisticsMapper;
        this.userMapper = userMapper;
        this.permissionService = permissionService;
    }

    public Map<String, Object> familyOverview() {
        Long userId = AuthContext.getUserId();
        User me = userMapper.findById(userId);
        if (me == null || me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requirePermission(me.getFamilyId(), userId);

        Map<String, Object> raw = statisticsMapper.familyOverview(me.getFamilyId());
        int total = parseInt(raw.get("totalCount"));
        int done = parseInt(raw.get("doneCount"));
        int overdue = parseInt(raw.get("overdueCount"));
        Map<String, Object> resp = new HashMap<>();
        resp.put("totalCount", total);
        resp.put("doneCount", done);
        resp.put("overdueCount", overdue);
        resp.put("completionRate", total == 0 ? 0 : (done * 100 / total));
        resp.put("overdueRate", total == 0 ? 0 : (overdue * 100 / total));
        return resp;
    }

    public List<MemberTaskStat> memberTaskStats() {
        Long userId = AuthContext.getUserId();
        User me = userMapper.findById(userId);
        if (me == null || me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requirePermission(me.getFamilyId(), userId);
        return statisticsMapper.memberTaskStats(me.getFamilyId());
    }

    private int parseInt(Object o) {
        if (o == null) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }
}
