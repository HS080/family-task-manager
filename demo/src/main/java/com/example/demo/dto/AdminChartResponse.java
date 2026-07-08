package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminChartResponse {
    private List<ChartItem> userRoleDistribution;
    private List<ChartItem> userStatusDistribution;
    private List<ChartItem> activityTrend;

    @Data
    @AllArgsConstructor
    public static class ChartItem {
        private String name;
        private long value;
    }
}
