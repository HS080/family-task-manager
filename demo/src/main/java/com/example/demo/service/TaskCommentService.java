package com.example.demo.service;

import com.example.demo.entity.Reminder;
import com.example.demo.entity.Task;
import com.example.demo.entity.TaskComment;
import com.example.demo.entity.TaskCommentView;
import com.example.demo.entity.User;
import com.example.demo.mapper.ReminderMapper;
import com.example.demo.mapper.TaskCommentMapper;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskCommentService {

    private final TaskCommentMapper taskCommentMapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final ReminderMapper reminderMapper;

    public TaskCommentService(TaskCommentMapper taskCommentMapper,
                              TaskMapper taskMapper,
                              UserMapper userMapper,
                              ReminderMapper reminderMapper) {
        this.taskCommentMapper = taskCommentMapper;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.reminderMapper = reminderMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public TaskComment createComment(TaskComment comment) {
        taskCommentMapper.insert(comment);

        Task task = taskMapper.findById(comment.getTaskId());
        if (task == null) {
            return comment;
        }

        User commenter = userMapper.findById(comment.getUserId());
        String commenterName = commenter != null ? commenter.getNickname() : ("成员" + comment.getUserId());
        String taskTitle = task.getTitle() != null ? task.getTitle() : ("任务#" + task.getId());

        Set<Long> receivers = new LinkedHashSet<>();
        if (task.getAssigneeUserId() != null) {
            receivers.add(task.getAssigneeUserId());
        }
        if (task.getCreatorUserId() != null) {
            receivers.add(task.getCreatorUserId());
        }
        // 评论同步到提醒中心：参与人收到提醒，同时评论者自己也保留一条操作提醒
        receivers.add(comment.getUserId());

        String content = commenterName + " 评论了任务【" + taskTitle + "】";
        for (Long receiverId : receivers) {
            Reminder reminder = new Reminder();
            reminder.setFamilyId(task.getFamilyId());
            reminder.setUserId(receiverId);
            reminder.setTaskId(task.getId());
            reminder.setRemindType("COMMENT");
            reminder.setContent(content);
            reminder.setRemindAt(LocalDateTime.now());
            reminderMapper.insert(reminder);
        }

        return comment;
    }

    public List<TaskCommentView> listByTaskId(Long taskId) {
        return taskCommentMapper.listByTaskId(taskId);
    }
}
