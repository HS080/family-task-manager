package com.example.demo.mapper;

import com.example.demo.entity.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public interface ReminderMapper {

    @Insert("INSERT INTO t_reminder(family_id, user_id, task_id, remind_type, content, remind_at, is_read, is_deleted, created_at, updated_at) VALUES(#{familyId}, #{userId}, #{taskId}, #{remindType}, #{content}, #{remindAt}, 0, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reminder reminder);

    @Select("SELECT id, family_id AS familyId, user_id AS userId, task_id AS taskId, remind_type AS remindType, content, remind_at AS remindAt, is_read AS isRead, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_reminder WHERE user_id = #{userId} AND family_id = #{familyId} AND is_deleted = 0 ORDER BY is_read, remind_at DESC")
    List<Reminder> listByUserId(@Param("userId") Long userId, @Param("familyId") Long familyId);

    @Update("UPDATE t_reminder SET is_read = 1, updated_at = NOW() WHERE id = #{id} AND user_id = #{userId} AND is_deleted = 0")
    int markRead(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE t_reminder SET is_deleted = 1, updated_at = NOW() WHERE id = #{id} AND user_id = #{userId} AND is_deleted = 0")
    int close(@Param("id") Long id, @Param("userId") Long userId);
}
