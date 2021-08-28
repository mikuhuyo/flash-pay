package com.flash.user.api.dto.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "PrivilegeDTO", description = "权限信息")
@Data
public class PrivilegeDTO implements Serializable {


    @ApiModelProperty("权限id")
    private Long id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限编码")
    private String code;

    @ApiModelProperty("所属权限组id")
    private Long privilegeGroupId;

}
