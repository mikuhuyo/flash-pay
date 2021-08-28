package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "AccountQueryDTO", description = "账号查询条件")
@Data
public class AccountQueryDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("所属租户")
    private Long tenantId;


}
