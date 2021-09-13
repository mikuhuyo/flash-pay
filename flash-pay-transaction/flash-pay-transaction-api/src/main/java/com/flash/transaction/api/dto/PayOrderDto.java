package com.flash.transaction.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@ApiModel(value = "PayOrderDto", description = "")
public class PayOrderDto implements Serializable {

    private static final long serialVersionUID = -1932679425406365768L;

    private Long id;

    @ApiModelProperty(value = "聚合支付订单号")
    private String tradeNo;

    @ApiModelProperty(value = "所属商户")
    private Long merchantId;

    @ApiModelProperty(value = "商户下门店")
    private Long storeId;

    @ApiModelProperty(value = "所属应用")
    private String appId;

    @ApiModelProperty(value = "原始支付渠道编码")
    private String payChannel;

    @ApiModelProperty(value = "原始渠道商户id")
    private String payChannelMchId;

    @ApiModelProperty(value = "原始渠道商户应用id")
    private String payChannelMchAppId;

    @ApiModelProperty(value = "原始渠道订单号")
    private String payChannelTradeNo;

    @ApiModelProperty(value = "聚合支付的渠道")
    private String channel;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "商品标题")
    private String subject;

    @ApiModelProperty(value = "订单描述")
    private String body;

    @ApiModelProperty(value = "币种CNY")
    private String currency;

    @ApiModelProperty(value = "订单总金额, 单位为分")
    private Integer totalAmount;

    @ApiModelProperty(value = "用户自定义的参数,商户自定义数据")
    private String optional;

    @ApiModelProperty(value = "用于统计分析的数据,用户自定义")
    private String analysis;

    @ApiModelProperty(value = "特定渠道发起时额外参数")
    private String extra;

    @ApiModelProperty(value = "交易状态支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成,4-关闭")
    private String tradeState;

    @ApiModelProperty(value = "渠道支付错误码")
    private String errorCode;

    @ApiModelProperty(value = "渠道支付错误信息")
    private String errorMsg;

    @ApiModelProperty(value = "设备")
    private String device;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "订单过期时间")
    private Date expireTime;

    @ApiModelProperty(value = "支付成功时间")
    private Date paySuccessTime;


}
