package com.flash.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.AmountUtil;
import com.flash.common.util.EncryptUtil;
import com.flash.common.util.RandomUuidUtil;
import com.flash.merchant.api.IAppService;
import com.flash.merchant.api.IMerchantService;
import com.flash.paymentagent.api.IPayChannelAgentService;
import com.flash.paymentagent.api.conf.AliConfigParam;
import com.flash.paymentagent.api.dto.AlipayBean;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;
import com.flash.transaction.api.IPayChannelService;
import com.flash.transaction.api.ITransactionService;
import com.flash.transaction.api.dto.PayChannelParamDto;
import com.flash.transaction.api.dto.PayOrderDto;
import com.flash.transaction.api.dto.QRCodeDto;
import com.flash.transaction.convert.PayOrderConvert;
import com.flash.transaction.entity.PayOrder;
import com.flash.transaction.mapper.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;

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

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Reference
    private IPayChannelService iPayChannelService;
    @Reference
    private IPayChannelAgentService iPayChannelAgentService;

    /**
     * ?????????????????????????????????????????????
     *
     * @param payOrderDto ????????????
     * @return PayOrderDto
     * @throws BusinessException ???????????????
     */
    private PayOrderDto savePayOrder(PayOrderDto payOrderDto) throws BusinessException {
        PayOrder payOrder = PayOrderConvert.INSTANCE.dto2entity(payOrderDto);

        Calendar nowTime = Calendar.getInstance();
        Date nowDate = nowTime.getTime();
        // ????????????????????????
        payOrder.setCreateTime(nowDate);
        // ????????????????????????, ?????????30??????
        payOrder.setExpireTime(new Date(nowDate.getTime() + 1800000));
        // ??????????????????
        payOrder.setCurrency("CNY");
        // ??????????????????
        payOrder.setTradeState("0");
        payOrder.setTradeNo(RandomUuidUtil.getUUID());

        payOrderMapper.insert(payOrder);

        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }

    /**
     * ????????????
     *
     * @param tradeNo ?????????
     * @return PayOrderDto
     */
    private PayOrderDto queryPayOrder(String tradeNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getTradeNo, tradeNo));
        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }

    /**
     * ????????????
     *
     * @param tradeNo ?????????
     * @return PaymentResponseDTO<Object>
     */
    private PaymentResponseDTO<Object> aliPayH5(String tradeNo) {
        AlipayBean alipayBean = new AlipayBean();

        // ?????????????????????????????????
        PayOrderDto payOrderDTO = queryPayOrder(tradeNo);
        alipayBean.setOutTradeNo(tradeNo);
        alipayBean.setSubject(payOrderDTO.getSubject());
        // ?????????-???
        String totalAmount = null;
        try {
            // ???????????????
            totalAmount = AmountUtil.changeF2Y(payOrderDTO.getTotalAmount().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_300006);
        }

        alipayBean.setTotalAmount(totalAmount);
        alipayBean.setBody(payOrderDTO.getBody());
        alipayBean.setStoreId(payOrderDTO.getStoreId());
        alipayBean.setExpireTime("30m");

        // ????????????, ????????????, ????????????????????????????????????
        PayChannelParamDto payChannelParamDTO = iPayChannelService.queryPayChannelParamByChannel(payOrderDTO.getAppId(), payOrderDTO.getChannel(), "ALIPAY_WAP");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        // ?????????????????????
        AliConfigParam aliConfigParam = JSON.parseObject(payChannelParamDTO.getParam(), AliConfigParam.class);
        // ????????????
        aliConfigParam.setCharest("utf-8");
        PaymentResponseDTO<Object> payOrderResponse = iPayChannelAgentService.createPayOrderByAliWap(aliConfigParam, alipayBean);

        log.info("?????????H5????????????Content: {}", payOrderResponse.getContent());

        return payOrderResponse;
    }

    /**
     * ????????????, ??????????????????????????????
     *
     * @param merchantId ??????id
     * @param appId      ??????id
     * @param storeId    ??????id
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
    public void updateOrderTradeNoAndTradeState(String tradeNo, String payChannelTradeNo, String state) {
        final LambdaUpdateWrapper<PayOrder> lambda = new LambdaUpdateWrapper<PayOrder>();
        lambda.eq(PayOrder::getTradeNo, tradeNo).set(PayOrder::getPayChannelTradeNo, payChannelTradeNo).set(PayOrder::getTradeState, state);
        if (state != null && "2".equals(state)) {
            lambda.set(PayOrder::getPaySuccessTime, new Date());
        }
        payOrderMapper.update(null, lambda);
    }

    @Override
    public Object submitOrderByAliPay(PayOrderDto payOrderDto) throws BusinessException {
        payOrderDto.setChannel("ALIPAY_WAP");
        PayOrderDto savePayOrderDto = savePayOrder(payOrderDto);

        // ???????????????-?????????
        return aliPayH5(savePayOrderDto.getTradeNo());
    }

    @Override
    public String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException {
        verifyAppAndStore(qrCodeDto.getMerchantId(), qrCodeDto.getAppId(), qrCodeDto.getStoreId());

        // ??????????????????
        PayOrderDto payOrderDto = new PayOrderDto();
        payOrderDto.setMerchantId(qrCodeDto.getMerchantId());
        payOrderDto.setAppId(qrCodeDto.getAppId());
        payOrderDto.setStoreId(qrCodeDto.getStoreId());
        payOrderDto.setSubject(qrCodeDto.getSubject());
        payOrderDto.setBody(qrCodeDto.getBody());
        payOrderDto.setChannel("shanju_c2b");

        String jsonString = JSONObject.toJSONString(payOrderDto);
        log.info("?????????????????????????????????json -> {}", jsonString);

        // ????????????
        String ticket = EncryptUtil.encodeUTF8StringBase64(jsonString);
        // ????????????
        String pay = payUrl + ticket;
        log.info("???????????????????????? -> {}", pay);

        return pay;
    }
}
