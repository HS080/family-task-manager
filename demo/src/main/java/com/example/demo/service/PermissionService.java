package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Permission requirePermission(Long familyId, Long userId) {
        Permission permission = permissionMapper.findByFamilyAndUser(familyId, userId);
        if (permission == null) {
            throw new BusinessException(403, "无家庭访问权限");
        }
        return permission;
    }

    public void requireOwner(Long familyId, Long userId) {
        Permission permission = requirePermission(familyId, userId);
        if (!"OWNER".equals(permission.getRoleCode())) {
            throw new BusinessException(403, "仅家庭OWNER可操作");
        }
    }
}
