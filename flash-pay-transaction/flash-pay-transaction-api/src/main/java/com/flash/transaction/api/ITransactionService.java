package com.flash.transaction.api;

import com.flash.common.domain.BusinessException;
import com.flash.transaction.api.dto.QRCodeDto;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface ITransactionService {
    /**
     * 生成门店二维码
     *
     * @param qrCodeDto 信息
     * @return 二维码
     * @throws BusinessException 自定义异常
     */
    String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException;
}
