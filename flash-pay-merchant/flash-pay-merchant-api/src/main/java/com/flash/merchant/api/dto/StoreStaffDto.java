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
@ApiModel(value = "StoreStaffDto", description = "")
public class StoreStaffDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "门店标识")
    private Long storeId;

    @ApiModelProperty(value = "员工标识")
    private Long staffId;


}
