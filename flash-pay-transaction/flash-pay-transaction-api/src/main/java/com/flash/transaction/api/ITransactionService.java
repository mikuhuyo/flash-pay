package com.flash.transaction.api;

import com.flash.common.domain.BusinessException;
import com.flash.transaction.api.dto.PayOrderDto;
import com.flash.transaction.api.dto.QRCodeDto;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface ITransactionService {
    /**
     * 更新订单支付状态
     *
     * @param tradeNo           闪聚平台订单号
     * @param payChannelTradeNo 支付宝或微信的交易流水号
     * @param state             订单状态 交易状态支付状态,0‐订单生成,1‐支付中(目前未使用),2‐支付成功,4‐关闭,5‐失败
     */
    void updateOrderTradeNoAndTradeState(String tradeNo, String payChannelTradeNo, String state);

    /**
     * 提交订单
     *
     * @param payOrderDto 订单信息
     * @return PaymentResponseDTO<Object>
     * @throws BusinessException 自定义异常
     */
    // PaymentResponseDTO<Object> submitOrderByAliPay(PayOrderDto payOrderDto) throws BusinessException;
    Object submitOrderByAliPay(PayOrderDto payOrderDto) throws BusinessException;

    /**
     * 生成门店二维码
     *
     * @param qrCodeDto 信息
     * @return 二维码
     * @throws BusinessException 自定义异常
     */
    String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException;
}
