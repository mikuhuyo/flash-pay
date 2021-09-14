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
