package com.flash.merchant.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YueLiMin
 * @version 1.0.0
 * @since 11
 */
@Data
@ApiModel(value = "StaffRoleDTO", description = "角色信息")
public class StaffRoleDto implements Serializable {
    private static final long serialVersionUID = 6898488023382627607L;

    @ApiModelProperty("角色Id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色编码")
    private String code;

    @ApiModelProperty("角色所属租户")
    private Long tenantId;

    @ApiModelProperty("角色包含权限列表")
    private List<String> privilegeCodes = new ArrayList<>();
}
