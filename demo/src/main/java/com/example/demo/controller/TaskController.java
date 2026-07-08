package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.config.AuthContext;
import com.example.demo.dto.CreateTaskCommentRequest;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.UpdateTaskStatusRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.TaskActivity;
import com.example.demo.entity.TaskComment;
import com.example.demo.entity.TaskCommentView;
import com.example.demo.service.TaskActivityService;
import com.example.demo.service.TaskCommentService;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCommentService taskCommentService;
    private final TaskActivityService taskActivityService;

    public TaskController(TaskService taskService,
                          TaskCommentService taskCommentService,
                          TaskActivityService taskActivityService) {
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.taskActivityService = taskActivityService;
    }

    @PostMapping
    public ApiResponse<Task> createTask(@RequestBody @Valid CreateTaskRequest request) {
        Task task = taskService.createTask(request);

        TaskActivity activity = new TaskActivity();
        activity.setTaskId(task.getId());
        activity.setOperatorUserId(AuthContext.getUserId());
        activity.setActionType("CREATE");
        activity.setActionDesc("创建任务: " + task.getTitle());
        taskActivityService.createActivity(activity);

        return ApiResponse.success("任务创建成功", task);
    }

    @GetMapping
    public ApiResponse<List<Task>> listTasks(@RequestParam(defaultValue = "false") Boolean mineOnly) {
        return ApiResponse.success(taskService.listTasks(mineOnly));
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> detail(@PathVariable Long id) {
        return ApiResponse.success(taskService.getTaskDetail(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Task> updateStatus(@PathVariable Long id, @RequestBody @Valid UpdateTaskStatusRequest request) {
        Task task = taskService.updateStatus(id, request);

        TaskActivity activity = new TaskActivity();
        activity.setTaskId(id);
        activity.setOperatorUserId(AuthContext.getUserId());
        activity.setActionType("STATUS_CHANGE");
        activity.setActionDesc("任务状态更新为: " + request.getStatus());
        taskActivityService.createActivity(activity);

        return ApiResponse.success("任务状态更新成功", task);
    }

    @PostMapping("/{id}/comments")
    public ApiResponse<TaskComment> createComment(@PathVariable Long id,
                                                  @RequestBody @Valid CreateTaskCommentRequest request) {
        taskService.getTaskDetail(id);

        TaskComment comment = new TaskComment();
        comment.setTaskId(id);
        comment.setUserId(AuthContext.getUserId());
        comment.setContent(request.getContent().trim());
        TaskComment saved = taskCommentService.createComment(comment);

        TaskActivity activity = new TaskActivity();
        activity.setTaskId(id);
        activity.setOperatorUserId(AuthContext.getUserId());
        activity.setActionType("COMMENT");
        activity.setActionDesc("新增留言: " + request.getContent().trim());
        taskActivityService.createActivity(activity);

        return ApiResponse.success("评论发布成功", saved);
    }

    @GetMapping("/{id}/comments")
    public ApiResponse<List<TaskCommentView>> listComments(@PathVariable Long id) {
        taskService.getTaskDetail(id);
        return ApiResponse.success(taskCommentService.listByTaskId(id));
    }

    @GetMapping("/{id}/activities")
    public ApiResponse<List<TaskActivity>> listActivities(@PathVariable Long id) {
        taskService.getTaskDetail(id);
        return ApiResponse.success(taskActivityService.listByTaskId(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        TaskActivity activity = new TaskActivity();
        activity.setTaskId(id);
        activity.setOperatorUserId(AuthContext.getUserId());
        activity.setActionType("DELETE");
        activity.setActionDesc("删除任务");
        taskActivityService.createActivity(activity);

        return ApiResponse.success("任务删除成功", true);
    }
}
