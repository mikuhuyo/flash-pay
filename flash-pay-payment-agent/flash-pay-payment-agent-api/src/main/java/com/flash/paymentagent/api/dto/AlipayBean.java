package com.flash.paymentagent.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付实体对象
 * 根据支付宝接口协议
 *
 * @author yuelimin
 */
@Data
public class AlipayBean implements Serializable {

    private static final long serialVersionUID = -6349885695253146038L;

    /**
     * 商户订单号, 必填
     */
    private String outTradeNo;
    /**
     * 订单名称, 必填
     */
    private String subject;
    /**
     * 付款金额, 必填
     */
    private String totalAmount;

    /**
     * 产品编号, 必填
     */
    private String productCode;
    /**
     * 商品描述, 可空
     */
    private String body;
    /**
     * 超时时间参数
     */
    private String expireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String timestamp;
    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 订单附件数据,  格式为: SJPAY,平台商户号,Appid,门店,平台渠道编码
     */
    private String attach;

}
