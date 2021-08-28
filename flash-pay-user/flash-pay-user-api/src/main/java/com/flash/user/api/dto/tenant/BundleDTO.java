package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "BundleDTO", description = "套餐信息")
@Data
public class BundleDTO implements Serializable {

    @ApiModelProperty("主键, 套餐id")
    private Long id;

    @ApiModelProperty("套餐名称")
    private String name;

    @ApiModelProperty("套餐编码")
    private String code;

    @ApiModelProperty("租户类型编码")
    private String tenantTypeCode;

    @ApiModelProperty("套餐包含功能描述,JSON格式的角色与权限")
    private String ability;

    @ApiModelProperty("API调用次数/月")
    private Integer numberOfInvocation;

    @ApiModelProperty("并发数/秒")
    private Integer numberOfConcurrent;

    @ApiModelProperty("允许创建应用数量")
    private Integer numberOfApp;

    @ApiModelProperty("套餐说明")
    private String comment;

    @ApiModelProperty("是否为初始化套餐,1表示是初始化套餐, 0表示不是")
    private Boolean initialize;

}
