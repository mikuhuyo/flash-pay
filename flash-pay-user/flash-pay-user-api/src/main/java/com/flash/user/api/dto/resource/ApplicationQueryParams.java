package com.flash.user.api.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "ApplicationQueryParams", description = "应用查询参数")
public class ApplicationQueryParams {

    @ApiModelProperty("应用名称(模糊匹配)")
    private String name;

}
