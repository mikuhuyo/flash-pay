package com.flash.user.controller;

import com.flash.user.api.AuthorizationService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.authorization.PrivilegeDTO;
import com.flash.user.api.dto.authorization.PrivilegeTreeDTO;
import com.flash.user.api.dto.authorization.RoleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(value = "统一账号-角色权限", tags = "统一账号-角色权限", description = "统一账号-角色权限")
public class AuthorizationController {

    @Reference
    private AuthorizationService authService;


    @ApiOperation("授权, 获取某用户在多个租户下的权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tenantIds", value = "多个租户的id", required = true, allowMultiple = true, dataType = "Long", paramType = "query")
    })
    @GetMapping("/tenants/{username}/privileges")
    public Map<Long, AuthorizationInfoDTO> authorize(@PathVariable String username, @RequestParam Long[] tenantIds) {
        Map<Long, AuthorizationInfoDTO> authorize = authService.authorize(username, tenantIds);
        return authorize;
    }

    @ApiOperation("查找某租户下, 多个角色的权限信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "多个角色编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/tenants/{tenantId}/roles/privilege-list")
    public List<PrivilegeDTO> queryPrivilege(@PathVariable Long tenantId, @RequestParam String[] roleCodes) {
        return authService.queryPrivilege(tenantId, roleCodes);
    }

    @ApiOperation("获取权限组下所有权限")
    @ApiImplicitParam(name = "privilegeGroupId", value = "权限组的id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/privilege-groups/{privilegeGroupId}/privilege-list")
    public List<PrivilegeDTO> queryPrivilegeByGroupId(@PathVariable Long privilegeGroupId) {
        return authService.queryPrivilegeByGroupId(privilegeGroupId);
    }

    @ApiOperation("查找某租户下, 多个角色的权限信息集合, 并根据权限组组装成为权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "多个角色的编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/tenants/{tenantId}/roles/role-privilege-list")
    public PrivilegeTreeDTO queryPrivilegeTree(@PathVariable Long tenantId, @RequestParam String[] roleCodes) {
        return authService.queryPrivilegeTree(tenantId, roleCodes);
    }

    //////////////////////////////////////////////////角色, 权限///////////////////////////////////////////////////
    @ApiOperation("创建租户内角色(不包含权限)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "role", value = "角色信息", required = true, dataType = "RoleDTO", paramType = "body")
    })
    @PostMapping("/tenants/{tenantId}/roles")
    public void createRole(@PathVariable Long tenantId, @RequestBody RoleDTO role) {
        authService.createRole(tenantId, role);
    }

    @ApiOperation("根据角色编码删除租户内角色, 如果有账号绑定该角色, 禁止删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType = "path")
    })
    @Delete("/tenants/{tenantId}/roles/{roleCode}")
    public void removeRole(@PathVariable Long tenantId, @PathVariable String roleCode) {
        authService.removeRole(tenantId, roleCode);
    }

    @ApiOperation("修改租户内角色(不包含权限)")
    @ApiImplicitParam(name = "role", value = "角色信息", required = true, dataType = "RoleDTO", paramType = "body")
    @PutMapping("/tenants/roles")
    public void modifyRole(@RequestBody RoleDTO role) {
        authService.modifyRole(role);
    }

    @ApiOperation("角色设置权限(清除+设置)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "privilegeCodes", value = "权限编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @PutMapping("/tenants/{tenantId}/roles/{roleCode}/privileges")
    public void roleBindPrivilege(@PathVariable Long tenantId, @PathVariable String roleCode, @RequestParam String[] privilegeCodes) {
        authService.roleBindPrivilege(tenantId, roleCode, privilegeCodes);
    }

    @ApiOperation("查询某租户下角色(不分页, 不包含权限)")
    @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/tenants/{tenantId}/roles")
    public List<RoleDTO> queryRole(@PathVariable Long tenantId) {
        return authService.queryRole(tenantId);
    }

    @ApiOperation("查根据roleCode获取角色(不包含权限)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/roles/tenants/{tenantId}")
    public List<RoleDTO> queryRole(@PathVariable Long tenantId, @RequestParam String... roleCodes) {
        return authService.queryRole(tenantId, roleCodes);
    }

    @ApiOperation("获取租户内的某个角色信息(包含权限信息)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/tenants/{tenantId}/roles/{roleCode}/role-privilege")
    public RoleDTO queryTenantRole(@PathVariable Long tenantId, @PathVariable String roleCode) {
        return authService.queryTenantRole(tenantId, roleCode);
    }

    @ApiOperation("绑定角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/bind/tenants/{tenantId}/accounts/{username}/roles")
    public void bindAccountRole(@PathVariable String username, @PathVariable Long tenantId, @RequestParam String[] roleCodes) {
        authService.bindAccountRole(username, tenantId, roleCodes);
    }

    @ApiOperation("解绑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "roleCodes", value = "角色编码", required = true, allowMultiple = true, dataType = "String", paramType = "query")
    })
    @PutMapping("/unbind/tenants/{tenantId}/accounts/{username}/roles")
    public void unbindAccountRole(@PathVariable String username, @PathVariable Long tenantId, @RequestParam String[] roleCodes) {
        authService.unbindAccountRole(username, tenantId, roleCodes);
    }
}
