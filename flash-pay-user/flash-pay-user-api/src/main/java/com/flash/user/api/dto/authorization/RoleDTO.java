package com.flash.user.api.dto.authorization;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息
 */
@ApiModel(value = "RoleDTO", description = "角色信息")
@Data
public class RoleDTO implements Serializable {


    @ApiModelProperty("角色id")
    private Long id;
    /**
     * 角色名称
     */

    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 角色编码
     */

    @ApiModelProperty("角色编码")
    private String code;

    /**
     * 角色所属租户
     */

    @ApiModelProperty("角色所属租户")
    private Long tenantId;

    /**
     * 角色包含权限列表
     */

    @ApiModelProperty("角色包含权限列表")
    private List<String> privilegeCodes = new ArrayList<>();
}
