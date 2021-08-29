package com.flash.transaction.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.EncryptUtil;
import com.flash.merchant.api.IAppService;
import com.flash.merchant.api.IMerchantService;
import com.flash.transaction.api.ITransactionService;
import com.flash.transaction.api.dto.PayOrderDto;
import com.flash.transaction.api.dto.QRCodeDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service
public class ITransactionServiceImpl implements ITransactionService {
    @Value("${flash-pay.pay-url}")
    private String payUrl;

    @Reference
    private IMerchantService iMerchantService;
    @Reference
    private IAppService iAppService;

    /**
     * 校验应用, 门店是否属于指定商户
     *
     * @param merchantId 商户id
     * @param appId      应用id
     * @param storeId    门店id
     */
    private void verifyAppAndStore(Long merchantId, String appId, Long storeId) {
        if (!iMerchantService.queryStoreInMerchantId(storeId, merchantId)) {
            throw new BusinessException(CommonErrorCode.E_200006);
        }

        if (!iAppService.queryAppInMerchant(appId, merchantId)) {
            throw new BusinessException(CommonErrorCode.E_200005);
        }
    }

    @Override
    public String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException {
        verifyAppAndStore(qrCodeDto.getMerchantId(), qrCodeDto.getAppId(), qrCodeDto.getStoreId());

        // 生成支付信息
        PayOrderDto payOrderDto = new PayOrderDto();
        payOrderDto.setMerchantId(qrCodeDto.getMerchantId());
        payOrderDto.setAppId(qrCodeDto.getAppId());
        payOrderDto.setStoreId(qrCodeDto.getStoreId());
        payOrderDto.setSubject(qrCodeDto.getSubject());
        payOrderDto.setBody(qrCodeDto.getBody());
        payOrderDto.setChannel("shanju_c2b");

        String jsonString = JSONObject.toJSONString(payOrderDto);
        log.info("交易服务门店二维码生产json -> {}", jsonString);

        // 保存票据
        String ticket = EncryptUtil.encodeUTF8StringBase64(jsonString);
        // 支付入口
        String pay = payUrl + ticket;
        log.info("交易服务支付入口 -> {}", pay);

        return pay;
    }
}
