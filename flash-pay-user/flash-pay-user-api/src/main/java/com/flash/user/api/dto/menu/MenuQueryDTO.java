package com.flash.user.api.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "MenuQueryDTO", description = "菜单查询参数")
public class MenuQueryDTO implements Serializable {

    @ApiModelProperty("所属应用")
    private String applicationCode;

    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty("状态")
    private Integer status;


}
