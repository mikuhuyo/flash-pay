package com.flash.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@TableName("payment_bill")
public class PaymentBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户应用Id
     */
    private Long merchantAppId;

    /**
     * 商户订单号
     */
    private String merchantOrderNo;

    /**
     * 渠道订单号
     */
    private String channelOrderNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 交易时间
     */
    private String posTime;

    /**
     * 终端号
     */
    private String equipmentNo;

    /**
     * 用户账号/标识信息
     */
    private String userAccount;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 手续费
     */
    private BigDecimal serviceFee;

    /**
     * 退款单号
     */
    private String refundOrderNo;

    private BigDecimal refundMoney;

    /**
     * 平台支付渠道
     */
    private String platformChannel;

    /**
     * 备注
     */
    private String remark;


}
