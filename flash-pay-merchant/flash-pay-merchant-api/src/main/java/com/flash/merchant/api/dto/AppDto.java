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
@ApiModel(value = "AppDto", description = "")
public class AppDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "商店名称")
    private String appName;

    @ApiModelProperty(value = "所属商户")
    private Long merchantId;

    @ApiModelProperty(value = "应用公钥(RSAWithSHA256)")
    private String publicKey;

    @ApiModelProperty(value = "授权回调地址")
    private String notifyUrl;


}
