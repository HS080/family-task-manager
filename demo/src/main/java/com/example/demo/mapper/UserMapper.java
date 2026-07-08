package com.example.demo.mapper;

import com.example.demo.entity.FamilyMemberItem;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT id, user_uid AS userUid, username, password, nickname, role, avatar_url AS avatarUrl, CASE WHEN avatar_url IN ('FATHER','MOTHER','CHILD','OTHER') THEN avatar_url WHEN nickname LIKE '%爸%' THEN 'FATHER' WHEN nickname LIKE '%妈%' THEN 'MOTHER' WHEN nickname LIKE '%孩%' OR nickname LIKE '%子%' THEN 'CHILD' ELSE 'OTHER' END AS familyIdentity, family_id AS familyId, status, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_user WHERE username = #{username} AND is_deleted = 0 LIMIT 1")
    User findByUsername(@Param("username") String username);

    @Select("SELECT id, user_uid AS userUid, username, password, nickname, role, avatar_url AS avatarUrl, CASE WHEN avatar_url IN ('FATHER','MOTHER','CHILD','OTHER') THEN avatar_url WHEN nickname LIKE '%爸%' THEN 'FATHER' WHEN nickname LIKE '%妈%' THEN 'MOTHER' WHEN nickname LIKE '%孩%' OR nickname LIKE '%子%' THEN 'CHILD' ELSE 'OTHER' END AS familyIdentity, family_id AS familyId, status, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_user WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    User findById(@Param("id") Long id);

    @Select("SELECT id, user_uid AS userUid, username, password, nickname, role, avatar_url AS avatarUrl, CASE WHEN avatar_url IN ('FATHER','MOTHER','CHILD','OTHER') THEN avatar_url WHEN nickname LIKE '%爸%' THEN 'FATHER' WHEN nickname LIKE '%妈%' THEN 'MOTHER' WHEN nickname LIKE '%孩%' OR nickname LIKE '%子%' THEN 'CHILD' ELSE 'OTHER' END AS familyIdentity, family_id AS familyId, status, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_user WHERE user_uid = #{userUid} AND is_deleted = 0 LIMIT 1")
    User findByUserUid(@Param("userUid") String userUid);

    @Insert("INSERT INTO t_user(user_uid, username, password, nickname, role, avatar_url, family_id, status, is_deleted, created_at, updated_at) VALUES(#{userUid}, #{username}, #{password}, #{nickname}, #{role}, #{familyIdentity}, #{familyId}, 1, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT id, user_uid AS userUid, username, password, nickname, role, avatar_url AS avatarUrl, CASE WHEN avatar_url IN ('FATHER','MOTHER','CHILD','OTHER') THEN avatar_url WHEN nickname LIKE '%爸%' THEN 'FATHER' WHEN nickname LIKE '%妈%' THEN 'MOTHER' WHEN nickname LIKE '%孩%' OR nickname LIKE '%子%' THEN 'CHILD' ELSE 'OTHER' END AS familyIdentity, family_id AS familyId, status, is_deleted AS isDeleted, created_at AS createdAt, updated_at AS updatedAt FROM t_user WHERE is_deleted = 0 ORDER BY id ASC LIMIT #{size} OFFSET #{offset}")
    List<User> pageAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(1) FROM t_user WHERE is_deleted = 0")
    long countAll();

    @Update("UPDATE t_user SET status = #{status}, updated_at = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    @Select("SELECT COUNT(1) FROM t_user WHERE is_deleted = 0 AND role = 'ADMIN'")
    long countAdmins();

    @Select("SELECT COUNT(1) FROM t_user WHERE is_deleted = 0 AND role <> 'ADMIN'")
    long countNormalUsers();

    @Select("SELECT COUNT(1) FROM t_user WHERE is_deleted = 0 AND status = 1")
    long countActiveUsers();

    @Select("SELECT COUNT(1) FROM t_user WHERE is_deleted = 0 AND status = 0")
    long countDisabledUsers();

    @Update("UPDATE t_user SET family_id = #{familyId}, updated_at = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updateFamilyId(@Param("userId") Long userId, @Param("familyId") Long familyId);

    @Update("UPDATE t_user SET avatar_url = #{familyIdentity}, updated_at = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updateFamilyIdentity(@Param("userId") Long userId, @Param("familyIdentity") String familyIdentity);

    @Update("UPDATE t_user SET password = #{password}, updated_at = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);

    @Select("SELECT u.id AS userId, u.nickname, CASE WHEN u.avatar_url IN ('FATHER','MOTHER','CHILD','OTHER') THEN u.avatar_url WHEN u.nickname LIKE '%爸%' THEN 'FATHER' WHEN u.nickname LIKE '%妈%' THEN 'MOTHER' WHEN u.nickname LIKE '%孩%' OR u.nickname LIKE '%子%' THEN 'CHILD' ELSE 'OTHER' END AS familyIdentity, p.role_code AS roleCode, p.created_at AS joinedAt FROM t_user u JOIN t_permission p ON u.id = p.user_id AND p.is_deleted = 0 WHERE u.family_id = #{familyId} AND u.is_deleted = 0 ORDER BY p.role_code DESC, u.id ASC")
    List<FamilyMemberItem> listFamilyMembers(@Param("familyId") Long familyId);
}
