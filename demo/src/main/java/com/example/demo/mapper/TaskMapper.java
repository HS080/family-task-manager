package com.example.demo.mapper;

import com.example.demo.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO t_task(family_id, title, description, assignee_user_id, creator_user_id, status, priority, deadline, is_deleted, created_at, updated_at) VALUES(#{familyId}, #{title}, #{description}, #{assigneeUserId}, #{creatorUserId}, #{status}, #{priority}, #{deadline}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Task task);

    @Select("SELECT id, family_id AS familyId, title, description, assignee_user_id AS assigneeUserId, creator_user_id AS creatorUserId, status, priority, deadline, completed_at AS completedAt, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_task WHERE family_id = #{familyId} AND is_deleted = 0 ORDER BY deadline ASC, id DESC")
    List<Task> listByFamilyId(@Param("familyId") Long familyId);

    @Select("SELECT id, family_id AS familyId, title, description, assignee_user_id AS assigneeUserId, creator_user_id AS creatorUserId, status, priority, deadline, completed_at AS completedAt, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_task WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    Task findById(@Param("id") Long id);

    @Update("UPDATE t_task SET status = #{status}, completed_at = #{completedAt}, updated_at = NOW() WHERE id = #{id} AND is_deleted = 0")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("completedAt") java.time.LocalDateTime completedAt);

    @Update("UPDATE t_task SET title = #{title}, description = #{description}, assignee_user_id = #{assigneeUserId}, priority = #{priority}, deadline = #{deadline}, updated_at = NOW() WHERE id = #{id} AND is_deleted = 0")
    int updateTask(Task task);

    @Update("UPDATE t_task SET is_deleted = 1, updated_at = NOW() WHERE id = #{id} AND is_deleted = 0")
    int softDelete(@Param("id") Long id);

    @Select("SELECT COUNT(1) FROM t_task WHERE is_deleted = 0")
    long countAll();
}
