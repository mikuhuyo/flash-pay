package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "AccountRoleQueryDTO", description = "管理员信息")
@Data
public class AccountRoleQueryDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("账号名称")
    private String username;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("租户id")
    private Long tenantId;
}
