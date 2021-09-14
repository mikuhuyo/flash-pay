package com.flash.paymentagent.api;

import com.alipay.api.AlipayApiException;
import com.flash.common.domain.BusinessException;
import com.flash.paymentagent.api.conf.AliConfigParam;
import com.flash.paymentagent.api.dto.AlipayBean;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IPayChannelAgentService {
    /**
     * 调用手机支付宝wap支付接口
     *
     * @param aliConfigParam 支付渠道参数
     * @param alipayBean     请求支付参数
     * @return PaymentResponseDTO<Object>
     * @throws BusinessException 自定义异常
     */
    PaymentResponseDTO<Object> createPayOrderByAliWap(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException;
}
