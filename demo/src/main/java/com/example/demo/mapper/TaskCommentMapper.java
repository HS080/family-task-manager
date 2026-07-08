package com.example.demo.mapper;

import com.example.demo.entity.TaskComment;
import com.example.demo.entity.TaskCommentView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskCommentMapper {

    @Insert("INSERT INTO t_task_comment(task_id, user_id, content, created_at, updated_at) VALUES(#{taskId}, #{userId}, #{content}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TaskComment comment);

    @Select("SELECT c.id, c.task_id AS taskId, c.user_id AS userId, c.content, IFNULL(u.nickname, u.username) AS userNickname, c.created_at AS createdAt, c.updated_at AS updatedAt FROM t_task_comment c LEFT JOIN t_user u ON c.user_id = u.id WHERE c.task_id = #{taskId} ORDER BY c.id DESC")
    List<TaskCommentView> listByTaskId(@Param("taskId") Long taskId);

    @Delete("DELETE FROM t_task_comment WHERE task_id = #{taskId}")
    int deleteByTaskId(@Param("taskId") Long taskId);
}
