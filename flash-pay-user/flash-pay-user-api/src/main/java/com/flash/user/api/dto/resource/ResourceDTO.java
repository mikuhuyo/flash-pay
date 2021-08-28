package com.flash.user.api.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统资源
 */
@ApiModel(value = "ResourceDTO", description = "应用资源")
@Data
public class ResourceDTO implements Serializable {

    @ApiModelProperty("应用编码")
    String applicationCode;


    @ApiModelProperty("应用名称")
    String applicationName;

    @ApiModelProperty("应用包含资源, 按资源类型分包括菜单等资源信息 如  menu:JSONObject")
    Map<String, Object> appRes = new HashMap<>();
}
