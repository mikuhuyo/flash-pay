package com.flash.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@TableName("refund_order")
public class RefundOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 聚合支付退款订单号
     */
    @TableField("REFUND_NO")
    private String refundNo;

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
     * 原始渠道订单号
     */
    @TableField("PAY_CHANNEL_TRADE_NO")
    private String payChannelTradeNo;

    /**
     * 原始渠道退款订单号
     */
    @TableField("PAY_CHANNEL_REFUND_NO")
    private String payChannelRefundNo;

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
     * 商户退款订单号
     */
    @TableField("OUT_REFUND_NO")
    private String outRefundNo;

    /**
     * 原始渠道用户标识,如微信openId,支付宝账号
     */
    @TableField("PAY_CHANNEL_USER")
    private String payChannelUser;

    /**
     * 原始渠道用户姓名
     */
    @TableField("PAY_CHANNEL_USERNAME")
    private String payChannelUsername;

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
     * 退款金额,单位分
     */
    @TableField("REFUND_AMOUNT")
    private Integer refundAmount;

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
     * 退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成
     */
    @TableField("REFUND_STATE")
    private String refundState;

    /**
     * 退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败
     */
    @TableField("REFUND_RESULT")
    private String refundResult;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 订单过期时间
     */
    @TableField("EXPIRE_TIME")
    private LocalDateTime expireTime;

    /**
     * 退款成功时间
     */
    @TableField("REFUND_SUCCESS_TIME")
    private LocalDateTime refundSuccessTime;


}
