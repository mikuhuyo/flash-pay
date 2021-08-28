package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "TenRolePrivilegeDTO", description = "租户角色权限信息")
@Data
public class TenRolePrivilegeDTO implements Serializable {

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("权限编码")
    private String privilegeCode;
}
