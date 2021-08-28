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
@ApiModel(value = "PayChannelDto", description = "")
public class PayChannelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "原始支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "原始支付渠道编码")
    private String channelCode;


}
