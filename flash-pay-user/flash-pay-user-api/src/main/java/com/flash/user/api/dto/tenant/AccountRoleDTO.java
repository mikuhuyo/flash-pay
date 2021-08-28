package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "AccountDTO", description = "账号信息")
@Data
public class AccountRoleDTO {

    @ApiModelProperty("账号名称")
    private String username;
    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("租户id")
    private Long tenantId;
}
