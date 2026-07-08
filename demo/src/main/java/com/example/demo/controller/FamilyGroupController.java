package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.JoinFamilyByCodeRequest;
import com.example.demo.dto.UpdateFamilyNameRequest;
import com.example.demo.dto.UpdateMemberIdentityRequest;
import com.example.demo.dto.UpdateMemberRoleRequest;
import com.example.demo.entity.FamilyGroup;
import com.example.demo.entity.FamilyMemberItem;
import com.example.demo.service.FamilyGroupService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.demo.config.AuthContext.getUserId;

@RestController
@RequestMapping("/api/families")
public class FamilyGroupController {

    private final FamilyGroupService familyGroupService;

    public FamilyGroupController(FamilyGroupService familyGroupService) {
        this.familyGroupService = familyGroupService;
    }

    @GetMapping("/me")
    public ApiResponse<FamilyGroup> myFamily() {
        return ApiResponse.success(familyGroupService.myFamily());
    }

    @PatchMapping("/me/name")
    public ApiResponse<FamilyGroup> updateFamilyName(@RequestBody @Valid UpdateFamilyNameRequest request) {
        return ApiResponse.success("家庭名称更新成功", familyGroupService.updateFamilyName(request.getFamilyName()));
    }

    @PatchMapping("/me/invite-code/reset")
    public ApiResponse<Map<String, String>> resetInviteCode() {
        return ApiResponse.success(Map.of("inviteCode", familyGroupService.resetInviteCode()));
    }

    @PostMapping("/join")
    public ApiResponse<Boolean> joinFamilyByCode(@RequestBody @Valid JoinFamilyByCodeRequest request) {
        return ApiResponse.success("加入成功", familyGroupService.joinFamilyByCode(getUserId(), request.getInviteCode()));
    }

    @PostMapping("/me/exit")
    public ApiResponse<Boolean> exitFamily() {
        familyGroupService.exitFamily();
        return ApiResponse.success("已退出家庭并创建新的个人家庭", true);
    }

    @GetMapping("/me/members")
    public ApiResponse<List<FamilyMemberItem>> members() {
        return ApiResponse.success(familyGroupService.listMembers());
    }

    @DeleteMapping("/me/members/{memberUserId}")
    public ApiResponse<Boolean> removeMember(@PathVariable Long memberUserId) {
        return ApiResponse.success("成员已删除", familyGroupService.removeMember(memberUserId));
    }

    @PatchMapping("/me/members/{memberUserId}/role")
    public ApiResponse<Boolean> updateMemberRole(@PathVariable Long memberUserId,
                                                 @RequestBody @Valid UpdateMemberRoleRequest request) {
        return ApiResponse.success("成员角色已更新", familyGroupService.updateMemberRole(memberUserId, request.getRoleCode()));
    }

    @PatchMapping("/me/members/{memberUserId}/identity")
    public ApiResponse<Boolean> updateMemberIdentity(@PathVariable Long memberUserId,
                                                     @RequestBody @Valid UpdateMemberIdentityRequest request) {
        return ApiResponse.success("成员家庭身份已更新", familyGroupService.updateMemberIdentity(memberUserId, request.getFamilyIdentity()));
    }
}
