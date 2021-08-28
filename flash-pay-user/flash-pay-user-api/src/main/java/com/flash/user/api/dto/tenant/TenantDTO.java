package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "TenantDTO", description = "租户信息")
@Data
public class TenantDTO implements Serializable {

    @ApiModelProperty("租户id")
    private Long id;

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("租户类型编码")
    private String tenantTypeCode;

    @ApiModelProperty("套餐编码")
    private String bundleCode;


}
