package com.flash.paymentagent.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yuelimin
 */
@Data
public class PaymentBillDTO implements Serializable {

    private static final long serialVersionUID = -968538639711820134L;

    // private Long merchantId;
    // private String merchantName;
    // private Long merchantAppId;

    private String merchantOrderNo;
    private String channelOrderNo;
    private String productName;
    private String createTime;
    private String posTime;
    private String equipmentNo;
    private String userAccount;
    private BigDecimal totalAmount;
    private BigDecimal tradeAmount;
    private BigDecimal discountAmount;
    private BigDecimal serviceFee;
    private String refundOrderNo;
    private BigDecimal refundMoney;
    private String platformChannel;
    // 自己平台的商户编号, 再根据该编号来确认是否是本平台的订单
    // private String storeId;
    private String remark;

}
