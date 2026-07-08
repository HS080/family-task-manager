package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.FamilyGroup;
import com.example.demo.entity.Permission;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.FamilyGroupMapper;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final FamilyGroupMapper familyGroupMapper;
    private final PermissionMapper permissionMapper;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper,
                       FamilyGroupMapper familyGroupMapper,
                       PermissionMapper permissionMapper,
                       JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.familyGroupMapper = familyGroupMapper;
        this.permissionMapper = permissionMapper;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || user.getStatus() == 0) {
            throw new BusinessException(401, "账号不存在或已禁用");
        }
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(401, "账号或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId());
        LoginResponse data = new LoginResponse();
        data.setToken(token);
        data.setUserId(user.getId());
        data.setUserUid(user.getUserUid());
        data.setNickname(user.getNickname());
        data.setRole(user.getRole());
        data.setFamilyId(user.getFamilyId());
        return data;
    }

    public LoginResponse register(RegisterRequest request) {
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new BusinessException(400, "账号已存在");
        }

        User user = new User();
        user.setUserUid(generateUserUid());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setRole("USER");
        user.setFamilyIdentity(request.getFamilyIdentity());

        if ("CREATE".equals(request.getEntryMode())) {
            if (request.getFamilyName() == null || request.getFamilyName().isBlank()) {
                throw new BusinessException(400, "创建家庭时家庭名称不能为空");
            }
            FamilyGroup family = new FamilyGroup();
            family.setFamilyName(request.getFamilyName().trim());
            family.setInviteCode(generateInviteCode());
            family.setOwnerUserId(0L);
            familyGroupMapper.insert(family);

            user.setFamilyId(family.getId());
            userMapper.insert(user);

            familyGroupMapper.updateOwner(family.getId(), user.getId());
            permissionMapper.insert(buildPermission(family.getId(), user.getId(), "OWNER"));
        } else {
            if (request.getInviteCode() == null || request.getInviteCode().isBlank()) {
                throw new BusinessException(400, "加入家庭时邀请码不能为空");
            }
            FamilyGroup family = familyGroupMapper.findByInviteCode(request.getInviteCode().trim());
            if (family == null) {
                throw new BusinessException(404, "家庭邀请码不存在");
            }
            user.setFamilyId(family.getId());
            userMapper.insert(user);
            permissionMapper.insert(buildPermission(family.getId(), user.getId(), "MEMBER"));
        }

        String token = jwtUtil.generateToken(user.getId());
        LoginResponse data = new LoginResponse();
        data.setToken(token);
        data.setUserId(user.getId());
        data.setUserUid(user.getUserUid());
        data.setNickname(user.getNickname());
        data.setRole(user.getRole());
        data.setFamilyId(user.getFamilyId());
        return data;
    }

    private String generateUserUid() {
        final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();

        for (int retry = 0; retry < 8; retry++) {
            StringBuilder sb = new StringBuilder(9);
            for (int i = 0; i < 9; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            String candidate = sb.toString();
            if (userMapper.findByUserUid(candidate) == null) {
                return candidate;
            }
        }

        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 9).toLowerCase();
    }

    public Boolean resetPassword(String username, String newPassword) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(404, "账号不存在");
        }
        user.setPassword(newPassword);
        userMapper.updatePassword(user.getId(), newPassword);
        return true;
    }

    private Permission buildPermission(Long familyId, Long userId, String role) {
        Permission permission = new Permission();
        permission.setFamilyId(familyId);
        permission.setUserId(userId);
        permission.setRoleCode(role);
        return permission;
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
