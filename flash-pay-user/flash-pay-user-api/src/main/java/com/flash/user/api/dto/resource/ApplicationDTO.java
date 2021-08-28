package com.flash.user.api.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ApplicationDTO", description = "应用信息")
public class ApplicationDTO implements Serializable {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("应用编码,跨租户唯一")
    private String code;

    @ApiModelProperty("应用所属租户")
    private Long tenantId;

}
