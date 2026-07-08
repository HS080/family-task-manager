package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.MemberTaskStat;
import com.example.demo.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/family-overview")
    public ApiResponse<Map<String, Object>> familyOverview() {
        return ApiResponse.success(statisticsService.familyOverview());
    }

    @GetMapping("/member-task")
    public ApiResponse<List<MemberTaskStat>> memberTaskStats() {
        return ApiResponse.success(statisticsService.memberTaskStats());
    }
}
