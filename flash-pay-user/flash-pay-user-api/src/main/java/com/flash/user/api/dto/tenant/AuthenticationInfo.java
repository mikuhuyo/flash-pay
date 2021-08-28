package com.flash.user.api.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "AuthenticationInfo", description = "认证请求信息")
@Data
public class AuthenticationInfo implements Serializable {

    @ApiModelProperty("认证主体:如用户名, 手机号等")
    private String principal;

    @ApiModelProperty("凭证:如密码, 短信认证码等")
    private String certificate;

    @ApiModelProperty("认证类型: 短信快捷认证, 用户名密码认证, 二维码认证等")
    private String authenticationType;

    @ApiModelProperty("短信认证时需要传key到短信微服务校验, 格式: sms:3d21042d054548b08477142bbca95cfa")
    private String smsKey;

}
