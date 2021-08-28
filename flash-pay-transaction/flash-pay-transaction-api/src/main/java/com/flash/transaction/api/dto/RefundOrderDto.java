package com.flash.transaction.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@ApiModel(value = "RefundOrderDto", description = "")
public class RefundOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "聚合支付退款订单号")
    private String refundNo;

    @ApiModelProperty(value = "聚合支付订单号")
    private String tradeNo;

    @ApiModelProperty(value = "所属商户")
    private Long merchantId;

    @ApiModelProperty(value = "所属应用")
    private String appId;

    @ApiModelProperty(value = "原始支付渠道编码")
    private String payChannel;

    @ApiModelProperty(value = "原始渠道商户id")
    private String payChannelMchId;

    @ApiModelProperty(value = "原始渠道订单号")
    private String payChannelTradeNo;

    @ApiModelProperty(value = "原始渠道退款订单号")
    private String payChannelRefundNo;

    @ApiModelProperty(value = "聚合支付的渠道")
    private String channel;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "商户退款订单号")
    private String outRefundNo;

    @ApiModelProperty(value = "原始渠道用户标识,如微信openId,支付宝账号")
    private String payChannelUser;

    @ApiModelProperty(value = "原始渠道用户姓名")
    private String payChannelUsername;

    @ApiModelProperty(value = "币种CNY")
    private String currency;

    @ApiModelProperty(value = "订单总金额, 单位为分")
    private Integer totalAmount;

    @ApiModelProperty(value = "退款金额,单位分")
    private Integer refundAmount;

    @ApiModelProperty(value = "用户自定义的参数,商户自定义数据")
    private String optional;

    @ApiModelProperty(value = "用于统计分析的数据,用户自定义")
    private String analysis;

    @ApiModelProperty(value = "特定渠道发起时额外参数")
    private String extra;

    @ApiModelProperty(value = "退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成")
    private String refundState;

    @ApiModelProperty(value = "退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败")
    private String refundResult;

    @ApiModelProperty(value = "渠道支付错误码")
    private String errorCode;

    @ApiModelProperty(value = "渠道支付错误信息")
    private String errorMsg;

    @ApiModelProperty(value = "设备")
    private String device;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "订单过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "退款成功时间")
    private LocalDateTime refundSuccessTime;


}
