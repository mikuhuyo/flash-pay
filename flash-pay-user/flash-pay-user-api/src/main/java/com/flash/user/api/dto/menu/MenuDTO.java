package com.flash.user.api.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "MenuDTO", description = "菜单信息")
public class MenuDTO implements Serializable {

    @ApiModelProperty("菜单id")
    private Long id;

    @ApiModelProperty("菜单父级id")
    private Long parentId;

    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty("菜单跳转url")
    private String url;

    @ApiModelProperty("页面标识")
    private String code;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("说明")
    private String comment;

    /**
     * 所属应用标识, 作为资源的必要属性
     */
    @ApiModelProperty("所属应用编码")
    private String applicationCode;

    @ApiModelProperty("所属应用")
    private String application;
    /**
     * 绑定的权限标识, 作为资源的必要属性
     */
    @ApiModelProperty("绑定的权限编码")
    private String privilegeCode;

    @ApiModelProperty("状态")
    private Integer status;


}
