package com.flash.transaction.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@ApiModel(value = "PaymentBillDto", description = "")
public class PaymentBillDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "商户应用Id")
    private Long merchantAppId;

    @ApiModelProperty(value = "商户订单号")
    private String merchantOrderNo;

    @ApiModelProperty(value = "渠道订单号")
    private String channelOrderNo;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "交易时间")
    private String posTime;

    @ApiModelProperty(value = "终端号")
    private String equipmentNo;

    @ApiModelProperty(value = "用户账号/标识信息")
    private String userAccount;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实际交易金额")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "退款单号")
    private String refundOrderNo;

    private BigDecimal refundMoney;

    @ApiModelProperty(value = "平台支付渠道")
    private String platformChannel;

    @ApiModelProperty(value = "备注")
    private String remark;


}
