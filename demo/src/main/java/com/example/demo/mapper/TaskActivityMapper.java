package com.example.demo.mapper;

import com.example.demo.entity.TaskActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskActivityMapper {

    @Insert("INSERT INTO t_task_activity(task_id, operator_user_id, action_type, action_desc, created_at, updated_at) VALUES(#{taskId}, #{operatorUserId}, #{actionType}, #{actionDesc}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TaskActivity activity);

    @Select("SELECT a.id, a.task_id AS taskId, a.operator_user_id AS operatorUserId, a.action_type AS actionType, a.action_desc AS actionDesc, IFNULL(u.nickname, u.username) AS operatorNickname, a.created_at AS createdAt, a.updated_at AS updatedAt FROM t_task_activity a LEFT JOIN t_user u ON a.operator_user_id = u.id WHERE a.task_id = #{taskId} ORDER BY a.id DESC")
    List<TaskActivity> listByTaskId(@Param("taskId") Long taskId);

    @Delete("DELETE FROM t_task_activity WHERE task_id = #{taskId}")
    int deleteByTaskId(@Param("taskId") Long taskId);
}
