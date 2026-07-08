package com.example.demo.service;

import com.example.demo.entity.TaskActivity;
import com.example.demo.mapper.TaskActivityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskActivityService {

    private final TaskActivityMapper taskActivityMapper;

    public TaskActivityService(TaskActivityMapper taskActivityMapper) {
        this.taskActivityMapper = taskActivityMapper;
    }

    public TaskActivity createActivity(TaskActivity activity) {
        taskActivityMapper.insert(activity);
        return activity;
    }

    public List<TaskActivity> listByTaskId(Long taskId) {
        return taskActivityMapper.listByTaskId(taskId);
    }
}
