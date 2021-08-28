package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "AbilityDTO", description = "套餐包含功能描述,JSON格式的角色与权限")
@Data
public class AbilityDTO implements Serializable {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("权限(数组)")
    private String[] privileges;

}
