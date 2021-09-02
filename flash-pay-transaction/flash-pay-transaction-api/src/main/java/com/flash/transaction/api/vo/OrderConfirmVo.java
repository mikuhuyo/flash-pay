package com.flash.transaction.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 */
@Data
@ApiModel(value = "OrderConfirmVO", description = "订单确认信息")
public class OrderConfirmVo implements Serializable {

    private static final long serialVersionUID = -6252754123997621191L;

    /**
     * 应用id
     */
    private String appId;
    /**
     * 交易单号
     */
    private String tradeNo;
    /**
     * 微信openid
     */
    private String openId;
    /**
     * 门店id
     */
    private String storeId;
    /**
     * 服务类型
     */
    private String channel;
    /**
     * 订单描述
     */
    private String body;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 金额
     */
    private String totalAmount;
}
