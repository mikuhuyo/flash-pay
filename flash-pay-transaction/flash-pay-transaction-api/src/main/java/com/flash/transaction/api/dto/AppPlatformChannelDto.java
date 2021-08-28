package com.flash.transaction.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@ApiModel(value = "AppPlatformChannelDto", description = "说明了应用选择了平台中的哪些支付渠道")
public class AppPlatformChannelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "应用id")
    private String appId;

    @ApiModelProperty(value = "平台支付渠道")
    private String platformChannel;


}
