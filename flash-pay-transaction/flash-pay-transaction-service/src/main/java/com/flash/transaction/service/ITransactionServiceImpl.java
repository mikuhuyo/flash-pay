package com.flash.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
     * 保存支付订单信息到闪聚支付平台
     *
     * @param payOrderDto 订单信息
     * @return PayOrderDto
     * @throws BusinessException 自定义异常
     */
    private PayOrderDto savePayOrder(PayOrderDto payOrderDto) throws BusinessException {
        PayOrder payOrder = PayOrderConvert.INSTANCE.dto2entity(payOrderDto);

        Calendar nowTime = Calendar.getInstance();
        Date nowDate = nowTime.getTime();
        // 设置订单创建时间
        payOrder.setCreateTime(nowDate);
        // 设置订单过期时间, 默认为30分钟
        payOrder.setExpireTime(new Date(nowDate.getTime() + 1800000));
        // 设置货币种类
        payOrder.setCurrency("CNY");
        // 设置订单状态
        payOrder.setTradeState("0");
        payOrder.setTradeNo(RandomUuidUtil.getUUID());

        payOrderMapper.insert(payOrder);

        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }

    /**
     * 查询订单
     *
     * @param tradeNo 订单号
     * @return PayOrderDto
     */
    private PayOrderDto queryPayOrder(String tradeNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getTradeNo, tradeNo));
        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }

    /**
     * 开始支付
     *
     * @param tradeNo 订单号
     * @return PaymentResponseDTO<Object>
     */
    private PaymentResponseDTO<Object> aliPayH5(String tradeNo) {
        AlipayBean alipayBean = new AlipayBean();

        // 根据订单号查询订单详情
        PayOrderDto payOrderDTO = queryPayOrder(tradeNo);
        alipayBean.setOutTradeNo(tradeNo);
        alipayBean.setSubject(payOrderDTO.getSubject());
        // 支付宝-元
        String totalAmount = null;
        try {
            // 将分转成元
            totalAmount = AmountUtil.changeF2Y(payOrderDTO.getTotalAmount().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_300006);
        }

        alipayBean.setTotalAmount(totalAmount);
        alipayBean.setBody(payOrderDTO.getBody());
        alipayBean.setStoreId(payOrderDTO.getStoreId());
        alipayBean.setExpireTime("30m");

        // 根据应用, 服务类型, 支付渠道查询支付渠道参数
        PayChannelParamDto payChannelParamDTO = iPayChannelService.queryPayChannelParamByChannel(payOrderDTO.getAppId(), payOrderDTO.getChannel(), "ALIPAY_WAP");
        if (payChannelParamDTO == null) {
            throw new BusinessException(CommonErrorCode.E_300007);
        }
        // 支付宝渠道参数
        AliConfigParam aliConfigParam = JSON.parseObject(payChannelParamDTO.getParam(), AliConfigParam.class);
        // 字符编码
        aliConfigParam.setCharest("utf-8");
        PaymentResponseDTO<Object> payOrderResponse = iPayChannelAgentService.createPayOrderByAliWap(aliConfigParam, alipayBean);

        log.info("支付宝H5支付响应Content: {}", payOrderResponse.getContent());

        return payOrderResponse;
    }

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
    public Object submitOrderByAliPay(PayOrderDto payOrderDto) throws BusinessException {
        payOrderDto.setChannel("ALIPAY_WAP");
        PayOrderDto savePayOrderDto = savePayOrder(payOrderDto);

        // 第三方支付-支付宝
        return aliPayH5(savePayOrderDto.getTradeNo());
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
