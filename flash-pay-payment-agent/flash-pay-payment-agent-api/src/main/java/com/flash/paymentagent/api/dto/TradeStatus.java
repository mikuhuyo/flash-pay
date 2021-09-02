package com.flash.paymentagent.api.dto;

/**
 * @author yuelimin
 */

public enum TradeStatus {
    /**
     * 业务交易支付 明确成功
     */
    SUCCESS,

    /**
     * 业务交易支付 明确失败
     */
    FAILED,

    /**
     * 业务交易状态未知
     */
    UNKNOWN,

    /**
     * 交易新建, 等待支付
     */
    USERPAYING,

    /**
     * 交易已撤销
     */
    REVOKED,
}
