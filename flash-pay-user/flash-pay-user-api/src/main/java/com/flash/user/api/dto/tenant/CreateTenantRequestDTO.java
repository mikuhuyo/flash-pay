package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "CreateTenantRequestDTO", description = "创建租户请求信息")
@Data
public class CreateTenantRequestDTO implements Serializable {

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("租户类型编码")
    private String tenantTypeCode;

    @ApiModelProperty("初始化套餐编码, 用于初始化套餐 ABILITY, 若为空则使用该租户类型的初始化套餐")
    private String bundleCode;

    @ApiModelProperty("租户管理员登录手机号")
    private String mobile;

    @ApiModelProperty("租户管理员登录密码")
    private String password;

    @ApiModelProperty("租户管理员登录用户名")
    private String username;


}
