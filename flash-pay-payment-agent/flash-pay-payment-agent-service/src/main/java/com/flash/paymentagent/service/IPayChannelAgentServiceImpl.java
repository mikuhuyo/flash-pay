package com.flash.paymentagent.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.paymentagent.api.IPayChannelAgentService;
import com.flash.paymentagent.api.conf.AliConfigParam;
import com.flash.paymentagent.api.dto.AlipayBean;
import com.flash.paymentagent.api.dto.PaymentResponseDTO;
import com.flash.paymentagent.api.dto.TradeStatus;
import com.flash.paymentagent.common.constant.AliCodeConstants;
import com.flash.paymentagent.message.PayProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service(interfaceClass = IPayChannelAgentService.class)
public class IPayChannelAgentServiceImpl implements IPayChannelAgentService {
    @Autowired
    private PayProducer payProducer;

    /**
     * 将支付宝查询时订单状态trade_status 转换为 闪聚订单状态
     *
     * @param aliTradeStatus 支付宝交易状态
     * @return TradeStatus
     */
    private TradeStatus covertAliTradeStatusToFlashPayCode(String aliTradeStatus) {
        switch (aliTradeStatus) {
            case AliCodeConstants.WAIT_BUYER_PAY:
                return TradeStatus.USERPAYING;
            case AliCodeConstants.TRADE_SUCCESS:
            case AliCodeConstants.TRADE_FINISHED:
                return TradeStatus.SUCCESS;
            default:
                return TradeStatus.FAILED;
        }
    }

    @Override
    public PaymentResponseDTO<Object> queryPayOrderByAli(AliConfigParam aliConfigParam, String outTradeNo) {
        String gateway = aliConfigParam.getUrl();
        String appId = aliConfigParam.getAppId();
        String rsaPrivateKey = aliConfigParam.getRsaPrivateKey();
        String format = aliConfigParam.getFormat();
        String charest = aliConfigParam.getCharest();
        String alipayPublicKey = aliConfigParam.getAlipayPublicKey();
        String signtype = aliConfigParam.getSigntype();

        log.info("C扫B请求支付宝查询订单, 参数: {}", JSON.toJSONString(aliConfigParam));

        // 构建sdk客户端
        AlipayClient client = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest, alipayPublicKey, signtype);
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        // 闪聚平台订单号
        model.setOutTradeNo(outTradeNo);
        // 封装请求参数
        queryRequest.setBizModel(model);
        PaymentResponseDTO<Object> dto;

        try {
            // 请求支付宝接口
            AlipayTradeQueryResponse qr = client.execute(queryRequest);
            // 接口调用成功
            if (AliCodeConstants.SUCCESSCODE.equals(qr.getCode())) {
                // 将支付宝响应的状态转换为闪聚平台的状态
                TradeStatus tradeStatus = covertAliTradeStatusToFlashPayCode(qr.getTradeStatus());
                dto = PaymentResponseDTO.success(qr.getTradeNo(), qr.getOutTradeNo(), tradeStatus, qr.getMsg() + " " + qr.getSubMsg());
                log.info("查询支付宝H5支付结果" + JSON.toJSONString(dto));
                return dto;
            }
        } catch (AlipayApiException e) {
            log.warn(e.getMessage(), e);
        }

        dto = PaymentResponseDTO.fail("查询支付宝支付结果异常", outTradeNo, TradeStatus.UNKNOWN);
        return dto;
    }

    @Override
    public PaymentResponseDTO<Object> createPayOrderByAliWap(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException {
        log.info("支付宝配置参数: {}", aliConfigParam.toString());
        log.info("支付宝请求参数: {}", alipayBean.toString());

        // 获取支付宝支付渠道参数
        // 支付宝下单接口地址
        String gateway = aliConfigParam.getUrl();
        // app id
        String appId = aliConfigParam.getAppId();
        // 私钥
        String rsaPrivateKey = aliConfigParam.getRsaPrivateKey();
        // 数据格式json
        String format = aliConfigParam.getFormat();
        // 字符编码
        String charest = aliConfigParam.getCharest();
        // 公钥
        String alipayPublicKey = aliConfigParam.getAlipayPublicKey();
        // 签名算法类型
        String signtype = aliConfigParam.getSigntype();
        // 支付结果通知地址
        String notifyUrl = aliConfigParam.getNotifyUrl();
        // 支付完成返回商户地址
        String returnUrl = aliConfigParam.getReturnUrl();

        // 支付宝sdk客户端
        // DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest, alipayPublicKey, signtype);
        DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(gateway, appId, rsaPrivateKey, format, charest, alipayPublicKey, "RSA");

        // 封装请求支付信息
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();


        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        // 闪聚平台订单
        model.setOutTradeNo(alipayBean.getOutTradeNo());
        // 订单标题
        model.setSubject(alipayBean.getSubject());
        // 订单金额
        model.setTotalAmount(alipayBean.getTotalAmount());
        // 订单过期时间
        model.setTimeoutExpress(alipayBean.getExpireTime());
        // 商户与支付宝签定的产品码, 固定为QUICK_WAP_WAY
        model.setProductCode(alipayBean.getProductCode());
        // 请求参数集合
        alipayRequest.setBizModel(model);

        // 设置同步地址
        alipayRequest.setReturnUrl(returnUrl);
        // 设置异步地址
        alipayRequest.setNotifyUrl(notifyUrl);

        try {
            // 发送支付结果查询延迟消息
            PaymentResponseDTO<AliConfigParam> notice = new PaymentResponseDTO<AliConfigParam>();
            notice.setOutTradeNo(alipayBean.getOutTradeNo());
            notice.setContent(aliConfigParam);
            notice.setMsg("ALIPAY_WAP");
            payProducer.payOrderNotice(notice);

            // 调用SDK提交表单
            AlipayTradeWapPayResponse alipayTradeWapPayResponse = defaultAlipayClient.pageExecute(alipayRequest);

            log.info("支付宝手机网站支付预支付订单信息: {}", alipayTradeWapPayResponse);

            PaymentResponseDTO<Object> res = new PaymentResponseDTO<Object>();
            res.setContent(alipayTradeWapPayResponse.getBody());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            // 支付宝确认支付失败
            throw new BusinessException(CommonErrorCode.E_400002);
        }
    }
}
