package com.flash.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@TableName("pay_order")
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 聚合支付订单号
     */
    @TableField("TRADE_NO")
    private String tradeNo;

    /**
     * 所属商户
     */
    @TableField("MERCHANT_ID")
    private Long merchantId;

    /**
     * 商户下门店
     */
    @TableField("STORE_ID")
    private Long storeId;

    /**
     * 所属应用
     */
    @TableField("APP_ID")
    private String appId;

    /**
     * 原始支付渠道编码
     */
    @TableField("PAY_CHANNEL")
    private String payChannel;

    /**
     * 原始渠道商户id
     */
    @TableField("PAY_CHANNEL_MCH_ID")
    private String payChannelMchId;

    /**
     * 原始渠道商户应用id
     */
    @TableField("PAY_CHANNEL_MCH_APP_ID")
    private String payChannelMchAppId;

    /**
     * 原始渠道订单号
     */
    @TableField("PAY_CHANNEL_TRADE_NO")
    private String payChannelTradeNo;

    /**
     * 聚合支付的渠道
     */
    @TableField("CHANNEL")
    private String channel;

    /**
     * 商户订单号
     */
    @TableField("OUT_TRADE_NO")
    private String outTradeNo;

    /**
     * 商品标题
     */
    @TableField("SUBJECT")
    private String subject;

    /**
     * 订单描述
     */
    @TableField("BODY")
    private String body;

    /**
     * 币种CNY
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 订单总金额, 单位为分
     */
    @TableField("TOTAL_AMOUNT")
    private Integer totalAmount;

    /**
     * 用户自定义的参数,商户自定义数据
     */
    @TableField("OPTIONAL")
    private String optional;

    /**
     * 用于统计分析的数据,用户自定义
     */
    @TableField("ANALYSIS")
    private String analysis;

    /**
     * 特定渠道发起时额外参数
     */
    @TableField("EXTRA")
    private String extra;

    /**
     * 交易状态支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成,4-关闭
     */
    @TableField("TRADE_STATE")
    private String tradeState;

    /**
     * 渠道支付错误码
     */
    @TableField("ERROR_CODE")
    private String errorCode;

    /**
     * 渠道支付错误信息
     */
    @TableField("ERROR_MSG")
    private String errorMsg;

    /**
     * 设备
     */
    @TableField("DEVICE")
    private String device;

    /**
     * 客户端IP
     */
    @TableField("CLIENT_IP")
    private String clientIp;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 订单过期时间
     */
    @TableField("EXPIRE_TIME")
    private Date expireTime;

    /**
     * 支付成功时间
     */
    @TableField("PAY_SUCCESS_TIME")
    private Date paySuccessTime;


}
