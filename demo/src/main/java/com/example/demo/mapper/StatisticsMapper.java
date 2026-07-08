package com.example.demo.mapper;

import com.example.demo.entity.MemberTaskStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    @Select("SELECT COUNT(1) AS totalCount, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS doneCount, " +
            "SUM(CASE WHEN status <> 2 AND deadline < NOW() THEN 1 ELSE 0 END) AS overdueCount " +
            "FROM t_task WHERE family_id = #{familyId} AND is_deleted = 0")
    Map<String, Object> familyOverview(@Param("familyId") Long familyId);

    @Select("SELECT t.assignee_user_id AS userId, IFNULL(u.nickname, u.username) AS nickname, " +
            "SUM(CASE WHEN t.status = 2 THEN 1 ELSE 0 END) AS completedCount " +
            "FROM t_task t LEFT JOIN t_user u ON t.assignee_user_id = u.id " +
            "WHERE t.family_id = #{familyId} AND t.is_deleted = 0 " +
            "GROUP BY t.assignee_user_id, u.nickname, u.username ORDER BY completedCount DESC")
    List<MemberTaskStat> memberTaskStats(@Param("familyId") Long familyId);
}
