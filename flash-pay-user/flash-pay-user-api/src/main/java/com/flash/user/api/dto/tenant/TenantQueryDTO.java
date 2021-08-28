package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "TenantQueryDTO", description = "租户查询条件")
@Data
public class TenantQueryDTO implements Serializable {

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("租户类型")
    private String tenantTypeCode;


}
