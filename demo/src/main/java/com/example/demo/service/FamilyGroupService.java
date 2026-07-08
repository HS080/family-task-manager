package com.example.demo.service;

import com.example.demo.config.AuthContext;
import com.example.demo.entity.FamilyGroup;
import com.example.demo.entity.FamilyMemberItem;
import com.example.demo.entity.Permission;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.FamilyGroupMapper;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FamilyGroupService {

    private final FamilyGroupMapper familyGroupMapper;
    private final UserMapper userMapper;
    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    public FamilyGroupService(FamilyGroupMapper familyGroupMapper,
                              UserMapper userMapper,
                              PermissionService permissionService,
                              PermissionMapper permissionMapper) {
        this.familyGroupMapper = familyGroupMapper;
        this.userMapper = userMapper;
        this.permissionService = permissionService;
        this.permissionMapper = permissionMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public FamilyGroup myFamily() {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            return createPersonalFamilyFor(me);
        }

        FamilyGroup family = familyGroupMapper.findById(me.getFamilyId());
        if (family == null) {
            return createPersonalFamilyFor(me);
        }

        Permission permission = permissionMapper.findByFamilyAndUser(me.getFamilyId(), me.getId());
        if (permission == null) {
            Permission owner = new Permission();
            owner.setFamilyId(me.getFamilyId());
            owner.setUserId(me.getId());
            owner.setRoleCode("OWNER");
            permissionMapper.insert(owner);
        }

        return family;
    }

    private FamilyGroup createPersonalFamilyFor(User me) {
        FamilyGroup newFamily = new FamilyGroup();
        newFamily.setFamilyName((me.getNickname() == null || me.getNickname().isBlank() ? "我的" : me.getNickname()) + "的小家");
        newFamily.setInviteCode(java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        newFamily.setOwnerUserId(me.getId());
        familyGroupMapper.insert(newFamily);
        familyGroupMapper.updateOwner(newFamily.getId(), me.getId());
        userMapper.updateFamilyId(me.getId(), newFamily.getId());

        Permission permission = new Permission();
        permission.setFamilyId(newFamily.getId());
        permission.setUserId(me.getId());
        permission.setRoleCode("OWNER");
        permissionMapper.insert(permission);
        return familyGroupMapper.findById(newFamily.getId());
    }

    public FamilyGroup updateFamilyName(String familyName) {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requireOwner(me.getFamilyId(), me.getId());
        familyGroupMapper.updateFamilyName(me.getFamilyId(), familyName.trim());
        return familyGroupMapper.findById(me.getFamilyId());
    }

    public String resetInviteCode() {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requireOwner(me.getFamilyId(), me.getId());

        String inviteCode = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        familyGroupMapper.resetInviteCode(me.getFamilyId(), inviteCode);
        return inviteCode;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean joinFamilyByCode(Long currentUserId, String inviteCode) {
        if (currentUserId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        String normalizedCode = inviteCode == null ? "" : inviteCode.trim().toUpperCase();
        if (normalizedCode.isEmpty()) {
            throw new BusinessException(400, "邀请码无效");
        }

        FamilyGroup targetFamily = familyGroupMapper.findByInviteCode(normalizedCode);
        if (targetFamily == null) {
            throw new BusinessException(400, "邀请码无效");
        }

        User currentUser = userMapper.findById(currentUserId);
        if (currentUser == null) {
            throw new BusinessException(401, "用户不存在或登录失效");
        }

        if (targetFamily.getId().equals(currentUser.getFamilyId())) {
            throw new BusinessException(400, "您已在该家庭中");
        }

        // 先清理该用户历史家庭角色关系
        permissionMapper.softDeleteByUserId(currentUserId);

        // 若历史上在该家庭已有记录（即使 is_deleted=1），则复用并恢复，避免唯一索引冲突
        int affected = permissionMapper.reactivateOrUpdateRole(targetFamily.getId(), currentUserId, "MEMBER");
        if (affected == 0) {
            Permission permission = new Permission();
            permission.setFamilyId(targetFamily.getId());
            permission.setUserId(currentUserId);
            permission.setRoleCode("MEMBER");
            permissionMapper.insert(permission);
        }

        userMapper.updateFamilyId(currentUserId, targetFamily.getId());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public FamilyGroup exitFamily() {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }

        Long fromFamilyId = me.getFamilyId();
        permissionService.requirePermission(fromFamilyId, me.getId());

        // 退出原家庭
        permissionMapper.softDelete(fromFamilyId, me.getId());

        // 自动创建“我的家庭”，保证一人一户，退出后仍可继续使用系统
        FamilyGroup newFamily = new FamilyGroup();
        newFamily.setFamilyName((me.getNickname() == null || me.getNickname().isBlank() ? "我的" : me.getNickname()) + "的小家");
        newFamily.setInviteCode(java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        newFamily.setOwnerUserId(me.getId());
        familyGroupMapper.insert(newFamily);

        familyGroupMapper.updateOwner(newFamily.getId(), me.getId());
        userMapper.updateFamilyId(me.getId(), newFamily.getId());

        Permission permission = new Permission();
        permission.setFamilyId(newFamily.getId());
        permission.setUserId(me.getId());
        permission.setRoleCode("OWNER");
        permissionMapper.insert(permission);

        return familyGroupMapper.findById(newFamily.getId());
    }

    public List<FamilyMemberItem> listMembers() {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requirePermission(me.getFamilyId(), me.getId());
        return userMapper.listFamilyMembers(me.getFamilyId());
    }

    public Boolean removeMember(Long memberUserId) {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requireOwner(me.getFamilyId(), me.getId());
        if (me.getId().equals(memberUserId)) {
            throw new BusinessException(400, "不能删除自己");
        }

        User target = userMapper.findById(memberUserId);
        if (target == null || !me.getFamilyId().equals(target.getFamilyId())) {
            throw new BusinessException(404, "成员不存在或不在当前家庭");
        }

        permissionMapper.softDelete(me.getFamilyId(), memberUserId);
        userMapper.updateFamilyId(memberUserId, null);
        return true;
    }

    public Boolean updateMemberRole(Long memberUserId, String roleCode) {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requireOwner(me.getFamilyId(), me.getId());
        if (me.getId().equals(memberUserId)) {
            throw new BusinessException(400, "不能修改自己的角色");
        }
        if (!"OWNER".equals(roleCode) && !"MEMBER".equals(roleCode)) {
            throw new BusinessException(400, "角色仅支持 OWNER 或 MEMBER");
        }

        User target = userMapper.findById(memberUserId);
        if (target == null || !me.getFamilyId().equals(target.getFamilyId())) {
            throw new BusinessException(404, "成员不存在或不在当前家庭");
        }

        permissionMapper.updateRole(me.getFamilyId(), memberUserId, roleCode);
        return true;
    }

    public Boolean updateMemberIdentity(Long memberUserId, String familyIdentity) {
        User me = currentUser();
        if (me.getFamilyId() == null) {
            throw new BusinessException(400, "当前用户未加入家庭");
        }
        permissionService.requireOwner(me.getFamilyId(), me.getId());

        User target = userMapper.findById(memberUserId);
        if (target == null || !me.getFamilyId().equals(target.getFamilyId())) {
            throw new BusinessException(404, "成员不存在或不在当前家庭");
        }

        userMapper.updateFamilyIdentity(memberUserId, familyIdentity);
        return true;
    }

    private User currentUser() {
        Long userId = AuthContext.getUserId();
        User me = userMapper.findById(userId);
        if (me == null) {
            throw new BusinessException(401, "用户不存在或登录失效");
        }
        return me;
    }
}
