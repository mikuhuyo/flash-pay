package com.flash.paymentagent.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yuelimin
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -8243175003527708618L;

    private Long id;
    private String tradeNo;//聚合支付订单号
    private Long merchantId;//所属商户
    private Long storeId;//商户下门店, 如果未指定, 默认是根门店
    private String appId;//此处使用业务id, 服务内部使用主键id, 服务与服务之间用业务id--appId
    private String payChannel;//原始支付渠道编码
    private String payChannelMchId;//原始渠道商户id
    private String payChannelTradeNo;//原始渠道订单号
    private String channel;//聚合支付的渠道 此处使用渠道编码
    private String outTradeNo;//商户订单号
    private String subject;//商品标题
    private String body;//订单描述
    private String currency;//币种CNY
    private Integer totalAmount;//订单总金额, 单位为分
    private String optional;//自定义数据
    private String analysis;//用于统计分析的数据,用户自定义
    private String extra;//特定渠道发起时额外参数
    private String tradeState;//交易状态支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成
    private String errorCode;//渠道支付错误码
    private String errorMsg;//渠道支付错误信息
    private String device;//设备
    private String clientIp;//客户端IP
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Date expireTime;//订单过期时间
    private Date paySuccessTime;//支付成功时间
    private String openId;
    /**
     * 原始商家的应用id
     */
    private String payChannelMchAppId;
    /**
     * 产品编号, 必填
     */
    private String productCode;

}
