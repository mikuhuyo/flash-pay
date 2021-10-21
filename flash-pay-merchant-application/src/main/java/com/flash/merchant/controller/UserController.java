package com.flash.merchant.controller;

import com.flash.user.api.AuthorizationService;
import com.flash.user.api.dto.authorization.RoleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@Api(value = "商户平台-租户内自管理", tags = "Role")
@RestController
public class UserController {

    @Reference
    private AuthorizationService authService;

    @GetMapping("/my/roles/tenants/{tenantId}")
    @ApiOperation("查询某租户下角色(不包含权限)")
    @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "path")
    public List<RoleDTO> queryRole(@PathVariable("tenantId") Long tenantId) {
        return authService.queryRole(tenantId);
    }
}
