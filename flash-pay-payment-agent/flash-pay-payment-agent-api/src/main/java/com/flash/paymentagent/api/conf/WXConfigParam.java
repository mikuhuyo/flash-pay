package com.flash.paymentagent.api.conf;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author yuelimin
 */
@Data
@NoArgsConstructor
public class WXConfigParam implements Serializable {

    private static final long serialVersionUID = -5806509163128668855L;

    private String appId;
    //是APPID对应的接口密码, 用于获取接口调用凭证access_token时使用.
    private String appSecret;
    private String mchId;
    private String key;
    public String returnUrl;

}
