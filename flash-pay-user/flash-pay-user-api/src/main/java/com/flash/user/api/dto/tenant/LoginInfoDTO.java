package com.flash.user.api.dto.tenant;

import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.resource.ResourceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(value = "LoginInfoDTO", description = "登录响应信息")
@Data
public class LoginInfoDTO implements Serializable {

    @ApiModelProperty("账号所属多个租户下, 不同应用的资源, 如: {租户A:[{应用1权限信息},{应用2权限信息}],租户B:[{},{}...]}")
    Map<Long, List<ResourceDTO>> resources = new HashMap<>();
    @ApiModelProperty("账号id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("账号所属多个租户")
    private List<TenantDTO> tenants;
    @ApiModelProperty("账号所属多个租户下的权限")
    private Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap = new HashMap<>();


}
