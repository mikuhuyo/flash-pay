package com.flash.merchant.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 1.8
 */
@Data
@ApiModel(value = "StoreDto", description = "")
public class StoreDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编号")
    private Long storeNumber;

    @ApiModelProperty(value = "所属商户")
    private Long merchantId;

    @ApiModelProperty(value = "父门店")
    private Long parentId;

    @ApiModelProperty(value = "0表示禁用, 1表示启用")
    private Boolean storeStatus;

    @ApiModelProperty(value = "门店地址")
    private String storeAddress;


}
