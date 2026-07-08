package com.example.demo.mapper;

import com.example.demo.entity.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Insert("INSERT INTO t_permission(family_id, user_id, role_code, is_deleted, created_at, updated_at) VALUES(#{familyId}, #{userId}, #{roleCode}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Permission permission);

    @Select("SELECT id, family_id AS familyId, user_id AS userId, role_code AS roleCode, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_permission WHERE family_id = #{familyId} AND user_id = #{userId} AND is_deleted = 0 LIMIT 1")
    Permission findByFamilyAndUser(@Param("familyId") Long familyId, @Param("userId") Long userId);

    @Select("SELECT id, family_id AS familyId, user_id AS userId, role_code AS roleCode, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_permission WHERE family_id = #{familyId} AND is_deleted = 0 ORDER BY id ASC")
    List<Permission> listByFamilyId(@Param("familyId") Long familyId);

    @Update("UPDATE t_permission SET is_deleted = 1, updated_at = NOW() WHERE family_id = #{familyId} AND user_id = #{userId} AND is_deleted = 0")
    int softDelete(@Param("familyId") Long familyId, @Param("userId") Long userId);

    @Update("UPDATE t_permission SET role_code = #{roleCode}, updated_at = NOW() WHERE family_id = #{familyId} AND user_id = #{userId} AND is_deleted = 0")
    int updateRole(@Param("familyId") Long familyId, @Param("userId") Long userId, @Param("roleCode") String roleCode);

    @Update("UPDATE t_permission SET role_code = #{roleCode}, is_deleted = 0, updated_at = NOW() WHERE family_id = #{familyId} AND user_id = #{userId}")
    int reactivateOrUpdateRole(@Param("familyId") Long familyId, @Param("userId") Long userId, @Param("roleCode") String roleCode);

    @Update("UPDATE t_permission SET is_deleted = 1, updated_at = NOW() WHERE user_id = #{userId} AND is_deleted = 0")
    int softDeleteByUserId(@Param("userId") Long userId);
}
