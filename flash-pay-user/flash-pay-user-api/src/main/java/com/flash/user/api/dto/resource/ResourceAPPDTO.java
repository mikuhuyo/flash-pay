package com.flash.user.api.dto.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceAPPDTO implements Serializable {

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("应用编码,跨租户唯一")
    private String code;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 菜单标题
     */
    @ApiModelProperty("菜单标题")
    private String title;

    /**
     * 链接url
     */
    @ApiModelProperty("链接url")
    private String url;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String comment;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 所属应用编码
     */
    @ApiModelProperty("所属应用编码")
    private String applicationCode;

    /**
     * 绑定权限
     */
    @ApiModelProperty("绑定权限")
    private String privilegeCode;
}
