package com.flash.merchant.api.vo;

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
@ApiModel(value = "MerchantRegisterVO", description = "商户注册信息")
public class MerchantRegistryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商户手机号")
    private String mobile;
    @ApiModelProperty("商户用户名")
    private String username;
    @ApiModelProperty("商户密码")
    private String password;
    @ApiModelProperty("商户短信秘钥")
    private String verifiykey;
    @ApiModelProperty("商户短信验证码")
    private String verifiyCode;
}
