package com.flash.paymentagent.api.dto;

import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;

import java.io.Serializable;

/**
 * @author yuelimin
 */
@Data
public class WeChatBean implements Serializable {

    private static final long serialVersionUID = 8517981834127171203L;

    private String appid;//公众号
    private String mchId;//商户号
    private String nonceStr;//随机字符串
    private String sign;//签名
    private String body;//商品描述
    private String outTradeNo;//商户订单号
    private Integer totalFee;//标价金额
    private String spbillCreateIp;//终端IP
    private String notifyUrl;//通知地址
    private String tradeType;//交易类型
    private String openId;
    private Long storeId;//自定义字段, 门店id
    private String attach;// 附加数据 attach 否

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        nonceStr = RandomStringUtils.random(10, "1234567890");
        this.nonceStr = nonceStr;
    }

}
