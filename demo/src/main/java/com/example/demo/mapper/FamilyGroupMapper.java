package com.example.demo.mapper;

import com.example.demo.entity.FamilyGroup;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FamilyGroupMapper {

    @Insert("INSERT INTO t_family_group(family_name, invite_code, owner_user_id, is_deleted, created_at, updated_at) VALUES(#{familyName}, #{inviteCode}, #{ownerUserId}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FamilyGroup familyGroup);

    @Select("SELECT id, family_name AS familyName, invite_code AS inviteCode, owner_user_id AS ownerUserId, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_family_group WHERE invite_code = #{inviteCode} AND is_deleted = 0 LIMIT 1")
    FamilyGroup findByInviteCode(@Param("inviteCode") String inviteCode);

    @Select("SELECT id, family_name AS familyName, invite_code AS inviteCode, owner_user_id AS ownerUserId, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_family_group WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    FamilyGroup findById(@Param("id") Long id);

    @Update("UPDATE t_family_group SET invite_code = #{inviteCode}, updated_at = NOW() WHERE id = #{familyId} AND is_deleted = 0")
    int resetInviteCode(@Param("familyId") Long familyId, @Param("inviteCode") String inviteCode);

    @Update("UPDATE t_family_group SET family_name = #{familyName}, updated_at = NOW() WHERE id = #{familyId} AND is_deleted = 0")
    int updateFamilyName(@Param("familyId") Long familyId, @Param("familyName") String familyName);

    @Update("UPDATE t_family_group SET owner_user_id = #{ownerUserId}, updated_at = NOW() WHERE id = #{familyId} AND is_deleted = 0")
    int updateOwner(@Param("familyId") Long familyId, @Param("ownerUserId") Long ownerUserId);

    @Select("SELECT id, family_name AS familyName, invite_code AS inviteCode, owner_user_id AS ownerUserId, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_family_group WHERE is_deleted = 0 ORDER BY id DESC LIMIT #{size} OFFSET #{offset}")
    java.util.List<FamilyGroup> pageAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(1) FROM t_family_group WHERE is_deleted = 0")
    long countAll();

    @Select("SELECT id, family_name AS familyName, invite_code AS inviteCode, owner_user_id AS ownerUserId, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_family_group WHERE is_deleted = 0 ORDER BY id DESC")
    java.util.List<FamilyGroup> findAll();
}
