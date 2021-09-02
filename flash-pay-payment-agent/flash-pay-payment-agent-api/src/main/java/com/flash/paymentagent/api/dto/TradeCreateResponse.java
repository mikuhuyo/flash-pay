package com.flash.paymentagent.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 预支付响应
 *
 * @author yuelimin
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeCreateResponse implements Serializable {

    private static final long serialVersionUID = 3559196596682273985L;

    /**
     * 原始支付渠道交易号
     * 目前了解的情况是:
     * 支付宝直接返回
     * 微信在支付结果通知才给返回
     */
    private String payChannelTradeNo;

    /**
     * 预支付响应内容 json字符串
     */
    private String responseContent;
}

